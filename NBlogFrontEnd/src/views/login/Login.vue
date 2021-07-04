<template>
  <div>
    <el-row type="flex" class="row-bg" justify="center">
      <el-col :span="8" class="main-box">
        <el-card class="box-card">
          <div slot="header">
            <span class="title"><i>NBlog </i>&nbsp;用户登录</span>
            <el-button @click="register" style="float: right; padding: 3px 0" type="text">注册</el-button>
          </div>

          <el-tabs v-model="activeName">
            <el-tab-pane label="账号密码登录" name="first">
              <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
                <el-form-item label="用户名" prop="name">
                  <el-col :span="15">
                    <el-input v-model="ruleForm.name"></el-input>
                  </el-col>
                </el-form-item>
                <el-form-item label="密码" prop="pwd">
                  <el-col :span="15">
                    <el-input v-model="ruleForm.pwd" show-password></el-input>
                  </el-col>
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="submitForm('ruleForm')">登录</el-button>
                  <el-button @click="resetForm('ruleForm')">重置</el-button>
                </el-form-item>
              </el-form>
            </el-tab-pane>
            <el-tab-pane label="邮箱快捷登录" name="second">
              <el-form :model="emailForm" :rules="emailrules" ref="emailForm" label-width="100px" class="demo-ruleForm">
                <el-form-item label="邮箱" prop="email">
                  <el-col :span="15">
                    <el-input v-model="emailForm.email"></el-input>
                  </el-col>
                </el-form-item>

                <el-form-item id="yzm-item" label="验证码" prop="yzm">
                  <el-col :span="10">
                    <el-input v-model="emailForm.yzm"></el-input>
                  </el-col>
                </el-form-item>
                <el-form-item>
                  <el-button id="yzm" type="primary" @click="submitForm('emailForm')">发送验证码</el-button>
                  <el-button @click="resetForm('emailForm')">重置</el-button>
                </el-form-item>
              </el-form>
            </el-tab-pane>
          </el-tabs>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
  let state="yzm"
  import {userLogin,emailLogin,emailExist} from "network/login";

  export default {
    name: "Login",
    data() {
      var checkEmail = (rule, value, callback) => {
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
        name:'',
        activeName: 'first',
        ruleForm: {
          name: '',
          pwd: '',
        },
        emailForm: {
          email: '',
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
        },
        emailrules: {
          email: [
            { required: true, message: '邮箱不能为空', trigger: 'blur' },
            { validator: checkEmail, trigger: 'blur' }
          ]
        }
      };
    },
    methods: {
      submitForm(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            if(formName==='emailForm'){
              if(state==='yzm'){
                emailExist(this.emailForm.email)
                  .then(res => {
                    if(res.data.code===200){
                      this.$message.error(res.data.extend.error)
                    }else{
                      this.$message({
                        message: '验证码已发送，注意查收',
                        type: 'success'
                      });

                      document.getElementById('yzm').innerHTML='登录'
                      document.getElementById('yzm-item').style.display='block'
                      state='login'

                      emailLogin(this.emailForm.email)
                        .then(res =>{
                          this.yzm=res.data.extend.code
                          this.name=res.data.extend.name
                        })
                        .catch(err => this.$message.error("登录失败："+err))
                    }
                  })
                  .catch(err => this.$message.error("登录失败："+err))
              }else {
                if(this.emailForm.yzm.toLowerCase()===this.yzm.toLowerCase()){
                  this.$store.state.user=this.name
                  localStorage.setItem('user',this.name)

                  this.$message({
                    message: '登录成功',
                    type: 'success'
                  });

                  setTimeout(()=>{
                    this.$router.push("/nblog/new")
                  },1000)
                }else {
                  this.$message.error("验证码错误")
                }
              }
            }else {
              userLogin(this.ruleForm.name,this.ruleForm.pwd)
                .then(res => {
                  if(res.data.code===100){
                    this.$store.state.user=this.ruleForm.name
                    localStorage.setItem('user',this.ruleForm.name)

                    this.$message({
                      message: '登录成功',
                      type: 'success'
                    });

                    setTimeout(()=>{
                      this.$router.push("/nblog/new")
                    },1000)


                  }else {
                    this.$message.error(res.data.extend.error)
                  }
                })
                .catch(err => this.$message.error("登录失败："+err))
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
      register(){
        this.$router.push('/nblog/register')
      }
    }
  }
</script>

<style scoped>
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
  .main-box{
    height: 350px;
  }
  #yzm-item{
    display: none;
  }
</style>
