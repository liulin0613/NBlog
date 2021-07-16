<template>
  <div>
    <el-row type="flex" class="row-bg" justify="center">
      <el-col :span="8">
        <el-card class="box-card">
          <div slot="header">
            <span class="title"><i>NBlog </i>&nbsp;用户注册</span>
            <el-button @click="login" style="float: right; padding: 3px 0" type="text">去登录</el-button>
          </div>

          <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
            <el-form-item label="用户名" prop="name">
              <el-col :span="12">
                <el-input v-model="ruleForm.name" @focus="nameChickFocus" @blur="nameChickBlur"  autocomplete="off"></el-input>
              </el-col>
              <el-col :span="1">&nbsp;
              </el-col>

              <el-col :span="6">
                <span id="has-register"> <i class="el-icon-close"></i> 已被注册</span>
                <span id="not-register" style="color:green;"> <i class="el-icon-check"></i> 可用</span>
              </el-col>
            </el-form-item>

            <el-form-item label="密码" prop="pwd">
              <el-col :span="12">
                <el-input v-model="ruleForm.pwd" show-password autocomplete="off"></el-input>
              </el-col>
            </el-form-item>

            <el-form-item label="确认密码" prop="repwd">
              <el-col :span="12">
                <el-input v-model="ruleForm.repwd" show-password @focus="rePwdCheakFocus" @blur="rePwdCheakBlur" autocomplete="off"></el-input>
              </el-col>

              <el-col :span="1">&nbsp;
              </el-col>

              <el-col :span="11">
                <span id="rePwd"> <i class="el-icon-close"></i> 两次密码不匹配</span>
              </el-col>
            </el-form-item>

            <el-form-item label="邮箱" prop="email">
              <el-col :span="12">
                <el-input v-model="ruleForm.email" autocomplete="off"></el-input>
              </el-col>

              <el-col :span="1">&nbsp;
              </el-col>

              <el-col :span="6">
                <el-button id="getYzm" type="primary" size="mini" round @click="getYzm">获取验证码</el-button>
              </el-col>
            </el-form-item>

            <el-form-item label="验证码" prop="yzm">
              <el-col :span="12">
                <el-input v-model="ruleForm.yzm" autocomplete="off" @blur="yzmBlur" @focus="yzmFocus"></el-input>
              </el-col>

              <el-col :span="1">&nbsp;
              </el-col>

              <el-col :span="11">
                <span id="yzmTip"> <i class="el-icon-close"></i> 验证码错误</span>
              </el-col>
            </el-form-item>

            <el-form-item>
              <el-button type="primary" @click="submitForm('ruleForm')">注册</el-button>
              <el-button @click="resetForm('ruleForm')">重置</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
  import {Checkname,emailYzm,addUser,emailExist} from "network/register";
  let timer = null;
  let count = 60;

  export default {
      name: "Register",
      data() {
        const checkEmail = (rule, value, callback) => {
          const mailReg = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/
          if (!value) {
            return callback(new Error('邮箱不能为空'))
          }
          setTimeout(() => {
            if (mailReg.test(value)) {
              callback()
            } else {
              callback(new Error('请输入正确的邮箱格式'))
            }
          }, 100)
        }

        return {
          yzm:'',
          ruleForm: {
            name: '',
            pwd: '',
            repwd:'',
            email:'',
            yzm:''
          },
          rules: {
            name: [
              { required: true, message: '请输入用户名', trigger: 'blur' },
              { min: 1, max: 6, message: '长度在 1 到 6 个字符', trigger: 'blur' }
            ],
            pwd: [
              { required: true, message: '请输入密码', trigger: 'blur' },
              { min: 6, max: 15, message: '长度在 6 到 15 个字符', trigger: 'blur' }
            ],
            repwd: [
              { required: true, message: '请输入密码', trigger: 'blur' },
            ],
            email: [
              { required: true, message: '请输入邮箱', trigger: 'blur' },
              { validator: checkEmail, trigger: 'blur' }
            ],
            yzm: [
              { required: true, message: '请输入验证码', trigger: 'blur' },
            ],


          }
        };
      },
      methods: {
        submitForm(formName) {
          this.$refs[formName].validate((valid) => {
            if (valid) {
              if(document.getElementById('has-register').style.display==='block'){
                this.$message.error("用户名已被注册")
              }else if(document.getElementById('rePwd').style.display==='block'){
                this.$message.error("两次密码不匹配")
              }else if(document.getElementById('yzmTip').style.display==='block'){
                this.$message.error("验证码错误")
              }else{
                addUser(this.ruleForm.name,this.ruleForm.pwd,this.ruleForm.email)
                  .then(res => {

                    clearInterval(timer);
                    this.$message({
                      message: '恭喜你，注册成功',
                      type: 'success'
                    });

                    setTimeout(()=>{
                      this.$router.push("/")
                    },1000)
                  })
                  .catch(err => this.$message.error("网络原因，注册失败"))
              }
            } else {
              console.log('error submit!!');
              return false;
            }
          });
        },
        resetForm(formName) {
          this.$refs[formName].resetFields();
        },
        login(){
          this.$router.push('/nblog/login')
        },
        nameChickBlur(){
          if(this.ruleForm.name.length>=1&&this.ruleForm.name.length<=6){
            Checkname(this.ruleForm.name)
              .then(res => {
                if(res.data.extend.count===0){
                  document.getElementById('not-register').style.display='block'
                }else {
                  document.getElementById('has-register').style.display='block'
                }
              })
              .catch(err => console.log(err))
          }
        },
        nameChickFocus(){
          document.getElementById('has-register').style.display='none'
          document.getElementById('not-register').style.display='none'
        },
        rePwdCheakBlur(){
          if(this.ruleForm.repwd!==this.ruleForm.pwd){
            document.getElementById('rePwd').style.display='block'
          }
        },
        rePwdCheakFocus(){
          document.getElementById('rePwd').style.display='none'
        },
        yzmBlur(){
          if(this.ruleForm.yzm.length!==0){
            if(this.ruleForm.yzm.toLowerCase()!==this.yzm.toLowerCase()){
              document.getElementById('yzmTip').style.display='block'
            }
          }
        },
        yzmFocus(){
          document.getElementById('yzmTip').style.display='none'
        },
        getYzm(){
          const myreg = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/;
          if(!myreg.test(this.ruleForm.email)) {

            return false;
          }else{
            emailExist(this.ruleForm.email)
              .then(res => {
                if(res.data.code===200){
                  count=60
                  this.$message({
                    message: '验证码已发送，请查收',
                    type: 'success'
                  });

                  document.getElementById('getYzm').disabled=true;

                  timer = setInterval(function(){
                    count--;
                    document.getElementById('getYzm').innerHTML=(--count+"(s)");
                    if (count <=0) {
                      clearInterval(timer);
                      document.getElementById('getYzm').disabled=false;
                      document.getElementById('getYzm').innerHTML=("重新获取验证码");
                    }
                  },1000);

                  emailYzm(this.ruleForm.email).then(res =>{
                    this.yzm=res.data.extend.code
                  }).catch(err => this.$message.error("注册失败："+err))

                }else{
                  this.$message.error("该邮箱已被注册")
                }
              })
              .catch(err => this.$message.error("注册失败："+err))
          }
        }
      }
    }
</script>

<style scoped>
  @media screen and (max-width: 900px) {
    .el-col-8 {
      width: 90%;
    }
  }

  .row-bg{
    display: flex;
    align-items:center;
    height: 700px;
  }
  .title{
    font-size: 22px;
  }

  .title>i{
    color: darkorange;
  }

  #has-register{
    display: none;
    color: red;
    font-size: 15px;
  }
  #not-register{
    display: none;
    color: green;
    font-size: 15px;
  }
  #rePwd{
    display: none;
    color: red;
  }
  #yzmTip{
    display: none;
    color: red;
  }
</style>
