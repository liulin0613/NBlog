<template>
  <div>
    <div style="padding:5px;">
      <Breadcrumb :route="breadcrumb"/>
      <el-container>
        <el-main>
          <el-card class="box-card">
            <div slot="header" class="clearfix">
              <span>NBlog</span>
            </div>
            <div>
              <el-row>
                <el-col :span="5">
                  <span>姓名：</span>
                  <span>{{this.$store.state.user}}</span>
                </el-col>
                <el-col :span="1">&nbsp;
                </el-col>
                <el-col :span="2">
                  <el-button type="text" @click="alertname">更改</el-button>
                </el-col>
                <el-col :span="1">&nbsp;
                </el-col>
                <el-col :span="5">
                  <span>拥有改名卡 ： {{count}}</span>
                </el-col>
                <el-col :span="1">&nbsp;
                </el-col>
                <el-col :span="8">
                  <span>Tip: 改名卡每三天获得一张，上限：1</span>
                </el-col>
              </el-row>
              <br>
              <el-row>
                <el-col :span="2">
                  <span>密码：</span>
                </el-col>
                <el-col :span="5">
                  <el-input :disabled="true" v-model="pwd" show-password></el-input>
                </el-col>
                <el-col :span="1">&nbsp;
                </el-col>
                <el-col :span="5">
                  <el-popover
                    placement="right"
                    width="400"
                    trigger="click">
                    <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
                      <el-form-item label="原密码" prop="pwd">
                        <el-input v-model="ruleForm.pwd" show-password></el-input>
                      </el-form-item>
                      <el-form-item label="新密码" prop="newpwd">
                        <el-input v-model="ruleForm.newpwd" show-password></el-input>
                      </el-form-item>
                      <el-form-item label="重新输入" prop="repwd">
                        <el-input v-model="ruleForm.repwd" show-password></el-input>
                      </el-form-item>
                      <el-form-item>
                        <el-button type="primary" @click="submitForm('ruleForm')">更改</el-button>
                        <el-button @click="resetForm('ruleForm')">重置</el-button>
                      </el-form-item>
                    </el-form>

                    <el-button slot="reference" type="text">更改</el-button>
                  </el-popover>

                </el-col>
              </el-row>
              <br><br>
              <el-row>
                <el-col :span="2">
                  <span>邮箱：</span>
                </el-col>
                <el-col :span="5">
                  <span>{{email}}</span>
                </el-col>
              </el-row>
              <br><br>
              <el-row>
                <el-col :span="2">
                  <span>状态：</span>
                </el-col>
                <el-col :span="5">
                  <span>正常使用</span>
                </el-col>
              </el-row>
            </div>
          </el-card>
        </el-main>
        <el-aside width="300px">
        </el-aside>
      </el-container>
    </div>
  </div>
</template>

<script>
  import Breadcrumb from "components/content/breadcrumb/Breadcrumb";
  import {getinfo,alertName,alertPwd} from "network/info";
  import {Checkname} from "network/register";

  export default {
    name: "InfoMain",
    components:{
      Breadcrumb
    },
    data(){
      return {
        breadcrumb:[
          {title:'首页',path:'/nblog/new'},
          {title:'个人中心'}
        ],
        pwd:'',
        email:'',
        count:0,
        ruleForm: {
          pwd: '',
          newpwd: '',
          repwd: '',
        },
        rules: {
          pwd: [
            { required: true, message: '请输入原密码', trigger: 'blur' },
          ],
          newpwd: [
            { required: true, message: '请输入新密码', trigger: 'change' },
            { min: 6, max: 15, message: '长度在 6 到 15 个字符', trigger: 'blur' }
          ],
          repwd: [
            { required: true, message: '请重新输入新密码', trigger: 'change' },
          ]
        }
      }
    },
    created() {
      getinfo(this.$store.state.user)
        .then(res => {
          this.pwd=res.data.extend.pwd
          this.email=res.data.extend.email
          this.count=res.data.extend.card
        })
        .catch(err => console.log(err))
    },
    methods:{
      alertname(){
        if(this.count===0){
          this.$message("改名卡数量不足")
        }else {
          this.$prompt('请输入姓名', '更改姓名', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            inputPattern: /^[[\s\S]{1,6}$/,
            inputErrorMessage: '姓名长度为1-6'
          }).then(({ value }) => {
            Checkname(value)
              .then(res => {
                if(res.data.extend.count===0){

                  alertName(this.$store.state.user,value)
                    .then(res => {
                      this.count=0

                      this.$store.commit('alertname',value)
                      this.$message({
                        type: 'success',
                        message: '更改成功'
                      });
                    })
                    .catch(err => console.log(err))

                }else {
                  this.$message("名字已被占用")
                }
              })
              .catch(err => console.log(err))

          }).catch(() => {
          });
        }
      },
      submitForm(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            if(this.ruleForm.pwd!==this.pwd){
              this.$message("原密码错误")
            }else if(this.ruleForm.repwd!==this.ruleForm.newpwd){
              this.$message("两次输入密码不一致")
            }else if(this.ruleForm.newpwd===this.pwd){
              this.$message("新密码与旧密码不能一致")
            }else {
              alertPwd(this.$store.state.user,this.ruleForm.newpwd)
                .then(res => {

                  this.pwd=this.ruleForm.newpwd
                  this.resetForm('ruleForm')

                  this.$message({
                    type:"success",
                    message:'修改成功'
                  })
                })
                .catch(err => console.log(err))

            }
          } else {
            console.log('error submit!!');
            return false;
          }
        });
      },
      resetForm(formName) {
        this.$refs[formName].resetFields();
      }
    }
  }
</script>

<style scoped>
  .el-row{
    display: flex;
    align-items: center;
  }
</style>
