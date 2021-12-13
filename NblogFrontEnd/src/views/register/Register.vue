<template>
  <el-row class="row-bg">
    <el-col>
        <el-form :model="ruleForm" :rules="rules" ref="ruleForm" :label-width="labelLen" class="demo-ruleForm">
          <el-row type="flex" class="row-bg" justify="center">
            <el-col :span="20">
              <el-form-item label="用户名" prop="name">
                <el-col :span="14"><el-input v-model="ruleForm.name" @input="nameCheck" @blur="nameCheck"></el-input></el-col>
                <el-col :span="1">&nbsp;</el-col> <!--占位-->
                <el-col :span="4" id="nameTipsInfo" :class="tipsInfo_style.nameClass"></el-col> <!--提示信息-->
              </el-form-item>
            </el-col>
          </el-row>

          <el-row type="flex" class="row-bg" justify="center">
            <el-col :span="20">
              <el-form-item label="密 码" prop="pwd">
                <el-col :span="14"><el-input v-model="ruleForm.pwd" show-password @input="pwdCheck" @blur="pwdCheck"></el-input></el-col>
                <el-col :span="1">&nbsp;</el-col> <!--占位-->
                <el-col :span="4" id="pwdTipsInfo" :class="tipsInfo_style.pwdClass"></el-col> <!--提示信息-->
              </el-form-item>
            </el-col>
          </el-row>

          <el-row type="flex" class="row-bg" justify="center">
            <el-col :span="20">
              <el-form-item label="确认密码" prop="rePwd">
                <el-col :span="14"><el-input v-model="ruleForm.rePwd" show-password @input="rePwdCheck" @blur="rePwdCheck"></el-input></el-col>
                <el-col :span="1">&nbsp;</el-col> <!--占位-->
                <el-col :span="4" id="rePwdTipsInfo" :class="tipsInfo_style.rePwdClass"></el-col> <!--提示信息-->
              </el-form-item>
            </el-col>
          </el-row>

          <el-row type="flex" class="row-bg" justify="center">
            <el-col :span="20">
              <el-form-item label="邮箱" prop="email">
                <el-col :span="14"><el-input v-model="ruleForm.email"></el-input></el-col>
                <el-col :span="1">&nbsp;</el-col> <!--占位-->
                <el-col :span="4"><el-button id="sendCode" type="primary" size="mini" round @click="emailCheckAndSendCode" @blur="emailCheckAndSendCode" :disabled="!canSend">获取验证码</el-button></el-col>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row type="flex" class="row-bg" justify="center">
            <el-col :span="20">
              <el-form-item label="验证码" prop="code">
                <el-col :span="14"><el-input v-model="ruleForm.code" @input="codeCheck" @blur="codeCheck"></el-input></el-col>
                <el-col :span="1">&nbsp;</el-col> <!--占位-->
                <el-col :span="4" id="codeTipsInfo" :class="tipsInfo_style.codeClass"></el-col> <!--提示信息-->
              </el-form-item>
            </el-col>
          </el-row>

          <el-row type="flex" class="row-bg" justify="center">
            <el-col :span="6">
              <el-form-item>
                <el-col :span="20" class="text-center"><el-button type="primary" round :disabled="!canSubmit" @click="submitForm()">注册</el-button></el-col>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
    </el-col>
  </el-row>
</template>

<script>
  import {checkName, emailExist, getCode, addUser} from '../../network/register';
  import{SHA256} from "../../network/login";

  export default {
    name: "Register",
    data() {
      return {
        labelLen: '',
        tipsInfo_style: {
          nameClass: '',
          pwdClass: '',
          rePwdClass: '',
          codeClass: '',
        },
        ruleForm: {
          name: '',
          pwd: '',
          rePwd: '',
          email: '',
          code: '',
        },
        rules: {
          name: [
            { required: true, message: '请输入用户名', trigger: 'blur' },
          ],
          pwd: [
            { required: true, message: '请输入密码', trigger: 'blur' },
          ],
          rePwd: [
            { required: true, message: '请再次输入密码', trigger: 'blur' },
          ],
          email: [
            { required: true, message: '请输入邮箱', trigger: 'blur' },
          ],
          code: [
            { required: true, message: '请输入验证码', trigger: 'blur' },
          ],
        },
        canSend: true, // 是否可以发送验证码
        timer: null, // 发送验证码后的计时器
        code: '', // 记录后端发送的验证码
        canSubmit: false, // 是否可以提交表单信息
      };
    },
    created() {
      /*this.adjustClientWidth();
      window.onresize = ()=> this.adjustClientWidth();*/
    },
    methods: {
      /*adjustClientWidth() {
        if (document.body.clientWidth >= 1920) {
          this.labelLen = '2vw';
        } else if (document.body.clientWidth >= 1200 ) {
          this.labelLen = '6vw';
        } else if (document.body.clientWidth >= 992) {
          this.labelLen = '8vw';
        } else if (document.body.clientWidth >= 768) {
          this.labelLen = '18vw';
        } else if (document.body.clientWidth >= 375) {
          this.labelLen = '24vw';
        }
      },*/
      toLogin() {
        this.$router.push('/NBlog/login');
      },
      nameCheck() {
        let name = this.ruleForm.name;
        const nameRule = /^[0-9a-zA-Z\u4e00-\u9fa5 ]+$/;
        let obj = document.getElementById('nameTipsInfo');
        if (name.length < 1 || name.length > 12) { // 检查长度
          this.tipsInfo_style.nameClass = 'fail';
          obj.innerText = '1~12个字符';
        } else if (!nameRule.test(name)) { // 检查是否符合命名规则
          this.tipsInfo_style.nameClass = 'fail';
          obj.innerText = '不符合命名规则';
          this.$message.error('只支持字母，数字和下划线');
        } else {
          this.nameBackCheck(name); // 后端检查该用户名是否存在
        }
      },
      nameBackCheck(name) {
        checkName(name).then(resp => {
          let obj = document.getElementById('nameTipsInfo');
          if (resp.data['data'] === 0) {
            obj.innerText = '√';
            this.tipsInfo_style.nameClass = 'success';
          } else if (resp.data['data'] === 1) {
            this.tipsInfo_style.nameClass = 'fail';
            obj.innerText = '已被注册';
          } else {
            this.tipsInfo_style.nameClass = 'fail';
            obj.innerText = '违规';
          }
        })
      },
      pwdCheck() {
        let pwd = this.ruleForm.pwd;
        let obj = document.getElementById('pwdTipsInfo');
        if (pwd.length < 3 || pwd.length > 15) {
          this.tipsInfo_style.pwdClass = 'fail';
          obj.innerText = '3~15个字符';
        } else {
          this.tipsInfo_style.pwdClass = 'success';
          obj.innerText = '√';
        }
      },
      rePwdCheck() {
        let rePwd = this.ruleForm.rePwd;
        let obj = document.getElementById('rePwdTipsInfo');
        if (rePwd === this.ruleForm.pwd && rePwd !== '') {
          this.tipsInfo_style.rePwdClass = 'success';
          obj.innerText = '√';
        } else {
          if(rePwd !== ''){
            this.tipsInfo_style.rePwdClass = 'fail';
            obj.innerText = '不一致';
          }
        }
      },
      emailCheckAndSendCode() {
        let email = this.ruleForm.email;
        const emailRule = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/;
        if (!emailRule.test(email)) {
          this.$message.error('请输入正确的邮箱格式');
        } else {
          this.emailBackCheck(email);
        }
      },
      emailBackCheck(email) {
        emailExist(email).then(resp => {
          if (resp.data['data'] === 0) {
            getCode(email).then(resp => {
              this.code = resp.data['data'];
              this.$message({
                message: '验证码已发送，请查收',
                type: 'success'});
              this.countDown();
            })
          } else if (resp.data['data'] === 1) {
            this.$message.error('该邮箱已被注册');
          }
        }).catch(() => this.$message.error('操作失败'))
      },
      countDown() {
        let obj = document.getElementById('sendCode');
        this.canSend = false;
        let count = 60;
        obj.innerText = count + ' s ';
        this.timer = setInterval(() => {
          count--;
          if (count >= 0) {
            obj.innerText = count + ' s';
          } else {
            clearInterval(this.timer);
            this.canSend = true;
            obj.innerText = '重新获取验证码';
          }
        }, 1000);
      },
      codeCheck() {
        let code = this.ruleForm.code;
        let obj = document.getElementById('codeTipsInfo');
        if (this.code.toLowerCase() === code.toLowerCase() && this.code !== '') {
          this.tipsInfo_style.codeClass = 'success';
          obj.innerText = '√';
          this.canSubmit = true;
        } else {
          this.tipsInfo_style.codeClass = 'fail';
          obj.innerText = '验证码错误';
        }
      },
      checkForm() { // 检查注册信息填写是否符合要求
        for (let key in this.tipsInfo_style) {
          if (this.tipsInfo_style[key] === 'fail') {
            return 0;
          }
        }
        return 1;
      },
      submitForm() {
        if (this.checkForm()) {
          addUser(this.ruleForm.name, SHA256(this.ruleForm.pwd), this.ruleForm.email).then(resp => {
            if (resp.data['code'] === 20000) {
              clearInterval(this.timer);
              this.$message.success('注册成功');

              // 重置表单
              this.$refs.ruleForm.resetFields()

              // 重置 Tips
              document.getElementById("nameTipsInfo").innerText='';
              document.getElementById("pwdTipsInfo").innerText='';
              document.getElementById("rePwdTipsInfo").innerText='';
              this.canSend = true;
              document.getElementById('sendCode').innerText = '获取验证码';
              document.getElementById("codeTipsInfo").innerText='';
              this.canSubmit = false;

              // toLogin
              document.getElementById("login").style.display = "block";
              document.getElementById("register").style.display = "none";

            } else {
              this.$message.error(resp.data['message']);
            }
          }).catch(() => this.$message.error('操作失败'));
        } else {
          this.$message.error('请先按提示正确填写注册信息');
        }
      },
    },
  }
</script>

<style scoped>
  /*错误信息提示样式*/
  .fail {
    color: red;
    font-style: italic;
  }
  /*正确信息提示样式*/
  .success {
    color: limegreen;
  }
</style>
