<template>
  <div id="app">

    <router-view/>
    <canvas class="noPrint" id="my_canvas"></canvas>

    <el-dialog
        title="登录"
        :visible="true"
        width="500px"
        :lock-scroll="false"
        center
        id="login"
        @close="closeLogin()"
      >

      <el-row type="flex" class="row-bg" justify="center">
        <el-col :span="22"><div>
          <el-form id="userLogin" :model="ruleForm" :rules="rules" ref="ruleForm" :label-width="labelLen" class="demo-ruleForm">
            <br>
            <el-row type="flex" class="row-bg" justify="center">
              <el-col :span="20">
                <div>
                  <el-form-item label="用户名" prop="name">
                    <el-col :span="18"><el-input v-model="ruleForm.name" placeholder="用户名 / 邮箱"></el-input></el-col>
                  </el-form-item>
                  <el-form-item label="密码" prop="pwd">
                    <el-col :span="18"><el-input v-model="ruleForm.pwd" show-password placeholder="密码"></el-input></el-col>
                  </el-form-item>
                </div>
              </el-col>
            </el-row>
            <br>
            <el-row type="flex" justify="center">
              <el-col :span="6">
                <div>
                  <el-button type="primary" @click="submitForm('ruleForm')">&nbsp;登录&nbsp;</el-button>
                </div>
              </el-col>
            </el-row>
          </el-form>

          <el-form id="emailLogin" :model="emailForm" :rules="emailRules" ref="emailForm" :label-width="labelLen" class="emailLogin-ruleForm">
            <el-row>
              <div class="return_user" @click="return_user">
                <el-col :span="1">&nbsp;</el-col>
                <el-col :span="5">< 返回</el-col>
              </div>

            </el-row>
            <br><br>
            <el-row type="flex" justify="center">
              <el-col :span="20">
                <div>
                  <el-form-item label="邮箱" prop="email">
                    <el-col :span="18"><el-input v-model="emailForm.email"></el-input></el-col>
                  </el-form-item>
                </div>
              </el-col>
            </el-row>

            <el-row type="flex" justify="center">
              <el-col :span="18">
                <div>
                  <el-form-item label="验证码" prop="code" v-show="showCodeInput">
                    <el-col :span="17"><el-input v-model="emailForm.code"></el-input></el-col>
                  </el-form-item>
                </div>
              </el-col>
            </el-row>

            <el-row type="flex" justify="center">
              <el-col :span="6">
                <div>
                  <el-button type="primary"  @click="sendYzm" v-show="!showCodeInput">发送验证码</el-button>
                  <el-button type="primary" @click="submitForm('emailForm')" v-show="showCodeInput">登录</el-button>
                </div>
              </el-col>
            </el-row>
          </el-form>

            <br><br>
            <el-row type="flex" justify="center">
              <el-col :span="9">
                <hr class="myhr">
              </el-col>
              <el-col :span="6">
                <span style="color: #c9c9c9">&nbsp;&nbsp;其他登录方式</span>
              </el-col>
              <el-col :span="9">
                <hr class="myhr">
              </el-col>
            </el-row>
            <br>
            <el-row type="flex" justify="center">
              <el-col :span="4" class="text-center">
                <el-button type="text" @click="showEmailLogin">
                  <svg t="1638791477928" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="1385" width="32" height="32"><path d="M873.472 0H150.528C67.584 0 0 67.584 0 150.528v722.944C0 956.416 67.584 1024 150.528 1024h722.944c82.944 0 150.528-67.584 150.528-150.528V150.528C1024 67.584 956.416 0 873.472 0z m-61.952 656.896c0 41.472-33.792 74.752-74.752 74.752H287.232c-41.472 0-74.752-33.28-74.752-74.752V367.104c0-41.472 33.28-74.752 74.752-74.752h449.024c41.472 0 74.752 33.28 74.752 74.752v289.792z" fill="#4873ED" p-id="1386"></path><path d="M712.192 427.008L512 556.032 311.808 427.008c-9.216-6.144-21.504-3.072-27.648 6.144-6.144 9.216-3.072 21.504 6.144 27.648l210.944 135.68c3.072 2.048 7.168 3.072 10.752 3.072 3.584 0 7.68-1.024 10.752-3.072L733.696 460.8a20.48 20.48 0 0 0 6.144-27.648 20.48 20.48 0 0 0-27.648-6.144z" fill="#4873ED" p-id="1387"></path></svg>
                </el-button>
              </el-col>
              <el-col :span="4" class="text-center">
                <el-button type="text">
                  <svg t="1637583832012" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="13459" width="32" height="32"><path d="M511.09761 957.257c-80.159 0-153.737-25.019-201.11-62.386-24.057 6.702-54.831 17.489-74.252 30.864-16.617 11.439-14.546 23.106-11.55 27.816 13.15 20.689 225.583 13.211 286.912 6.767v-3.061z" fill="#FAAD08" p-id="13460"></path><path d="M496.65061 957.257c80.157 0 153.737-25.019 201.11-62.386 24.057 6.702 54.83 17.489 74.253 30.864 16.616 11.439 14.543 23.106 11.55 27.816-13.15 20.689-225.584 13.211-286.914 6.767v-3.061z" fill="#FAAD08" p-id="13461"></path><path d="M497.12861 474.524c131.934-0.876 237.669-25.783 273.497-35.34 8.541-2.28 13.11-6.364 13.11-6.364 0.03-1.172 0.542-20.952 0.542-31.155C784.27761 229.833 701.12561 57.173 496.64061 57.162 292.15661 57.173 209.00061 229.832 209.00061 401.665c0 10.203 0.516 29.983 0.547 31.155 0 0 3.717 3.821 10.529 5.67 33.078 8.98 140.803 35.139 276.08 36.034h0.972z" fill="#000000" p-id="13462"></path><path d="M860.28261 619.782c-8.12-26.086-19.204-56.506-30.427-85.72 0 0-6.456-0.795-9.718 0.148-100.71 29.205-222.773 47.818-315.792 46.695h-0.962C410.88561 582.017 289.65061 563.617 189.27961 534.698 185.44461 533.595 177.87261 534.063 177.87261 534.063 166.64961 563.276 155.56661 593.696 147.44761 619.782 108.72961 744.168 121.27261 795.644 130.82461 796.798c20.496 2.474 79.78-93.637 79.78-93.637 0 97.66 88.324 247.617 290.576 248.996a718.01 718.01 0 0 1 5.367 0C708.80161 950.778 797.12261 800.822 797.12261 703.162c0 0 59.284 96.111 79.783 93.637 9.55-1.154 22.093-52.63-16.623-177.017" fill="#000000" p-id="13463"></path><path d="M434.38261 316.917c-27.9 1.24-51.745-30.106-53.24-69.956-1.518-39.877 19.858-73.207 47.764-74.454 27.875-1.224 51.703 30.109 53.218 69.974 1.527 39.877-19.853 73.2-47.742 74.436m206.67-69.956c-1.494 39.85-25.34 71.194-53.24 69.956-27.888-1.238-49.269-34.559-47.742-74.435 1.513-39.868 25.341-71.201 53.216-69.974 27.909 1.247 49.285 34.576 47.767 74.453" fill="#FFFFFF" p-id="13464"></path><path d="M683.94261 368.627c-7.323-17.609-81.062-37.227-172.353-37.227h-0.98c-91.29 0-165.031 19.618-172.352 37.227a6.244 6.244 0 0 0-0.535 2.505c0 1.269 0.393 2.414 1.006 3.386 6.168 9.765 88.054 58.018 171.882 58.018h0.98c83.827 0 165.71-48.25 171.881-58.016a6.352 6.352 0 0 0 1.002-3.395c0-0.897-0.2-1.736-0.531-2.498" fill="#FAAD08" p-id="13465"></path><path d="M467.63161 256.377c1.26 15.886-7.377 30-19.266 31.542-11.907 1.544-22.569-10.083-23.836-25.978-1.243-15.895 7.381-30.008 19.25-31.538 11.927-1.549 22.607 10.088 23.852 25.974m73.097 7.935c2.533-4.118 19.827-25.77 55.62-17.886 9.401 2.07 13.75 5.116 14.668 6.316 1.355 1.77 1.726 4.29 0.352 7.684-2.722 6.725-8.338 6.542-11.454 5.226-2.01-0.85-26.94-15.889-49.905 6.553-1.579 1.545-4.405 2.074-7.085 0.242-2.678-1.834-3.786-5.553-2.196-8.135" fill="#000000" p-id="13466"></path><path d="M504.33261 584.495h-0.967c-63.568 0.752-140.646-7.504-215.286-21.92-6.391 36.262-10.25 81.838-6.936 136.196 8.37 137.384 91.62 223.736 220.118 224.996H506.48461c128.498-1.26 211.748-87.612 220.12-224.996 3.314-54.362-0.547-99.938-6.94-136.203-74.654 14.423-151.745 22.684-215.332 21.927" fill="#FFFFFF" p-id="13467"></path><path d="M323.27461 577.016v137.468s64.957 12.705 130.031 3.91V591.59c-41.225-2.262-85.688-7.304-130.031-14.574" fill="#EB1C26" p-id="13468"></path><path d="M788.09761 432.536s-121.98 40.387-283.743 41.539h-0.962c-161.497-1.147-283.328-41.401-283.744-41.539l-40.854 106.952c102.186 32.31 228.837 53.135 324.598 51.926l0.96-0.002c95.768 1.216 222.4-19.61 324.6-51.924l-40.855-106.952z" fill="#EB1C26" p-id="13469"></path></svg>
                  <span style="color: #c9c9c9">（开发中）</span>
                </el-button>

              </el-col>
<!--              <el-col :span="4" class="text-center">-->
<!--                <el-button type="text">-->
<!--                  <svg t="1637583873745" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="14292" width="32" height="32"><path d="M851.4 590.193c-22.196-66.233-90.385-90.422-105.912-91.863-15.523-1.442-29.593-9.94-19.295-27.505 10.302-17.566 29.304-68.684-7.248-104.681-36.564-36.14-116.512-22.462-173.094 0.866-56.434 23.327-53.39 7.055-51.65-8.925 1.89-16.848 32.355-111.02-60.791-122.395C311.395 220.86 154.85 370.754 99.572 457.15 16 587.607 29.208 675.873 29.208 675.873h0.58c10.009 121.819 190.787 218.869 412.328 218.869 190.5 0 350.961-71.853 398.402-169.478 0 0 0.143-0.433 0.575-1.156 4.938-10.506 8.71-21.168 11.035-32.254 6.668-26.205 11.755-64.215-0.728-101.66z m-436.7 251.27c-157.71 0-285.674-84.095-285.674-187.768 0-103.671 127.82-187.76 285.674-187.76 157.705 0 285.673 84.089 285.673 187.76 0 103.815-127.968 187.768-285.673 187.768z" fill="#E71F19" p-id="14293"></path><path d="M803.096 425.327c2.896 1.298 5.945 1.869 8.994 1.869 8.993 0 17.7-5.328 21.323-14.112 5.95-13.964 8.993-28.793 8.993-44.205 0-62.488-51.208-113.321-114.181-113.321-15.379 0-30.32 3.022-44.396 8.926-11.755 4.896-17.263 18.432-12.335 30.24 4.933 11.662 18.572 17.134 30.465 12.238 8.419-3.46 17.268-5.33 26.41-5.33 37.431 0 67.752 30.241 67.752 67.247 0 9.068-1.735 17.857-5.369 26.202a22.832 22.832 0 0 0 12.335 30.236l0.01 0.01z" fill="#F5AA15" p-id="14294"></path><path d="M726.922 114.157c-25.969 0-51.65 3.744-76.315 10.942-18.423 5.472-28.868 24.622-23.5 42.91 5.509 18.29 24.804 28.657 43.237 23.329a201.888 201.888 0 0 1 56.578-8.064c109.253 0 198.189 88.271 198.189 196.696 0 19.436-2.905 38.729-8.419 57.16-5.508 18.289 4.79 37.588 23.212 43.053 3.342 1.014 6.817 1.442 10.159 1.442 14.943 0 28.725-9.648 33.37-24.48 7.547-24.906 11.462-50.826 11.462-77.175-0.143-146.588-120.278-265.813-267.973-265.813z" fill="#F5AA15" p-id="14295"></path><path d="M388.294 534.47c-84.151 0-152.34 59.178-152.34 132.334 0 73.141 68.189 132.328 152.34 132.328 84.148 0 152.337-59.182 152.337-132.328 0-73.15-68.19-132.334-152.337-132.334zM338.53 752.763c-29.454 0-53.39-23.755-53.39-52.987 0-29.228 23.941-52.989 53.39-52.989 29.453 0 53.39 23.76 53.39 52.989 0 29.227-23.937 52.987-53.39 52.987z m99.82-95.465c-6.382 11.086-19.296 15.696-28.726 10.219-9.43-5.323-11.75-18.717-5.37-29.803 6.386-11.09 19.297-15.7 28.725-10.224 9.43 5.472 11.755 18.864 5.37 29.808z" fill="#040000" p-id="14296"></path></svg>-->
<!--                </el-button>-->
<!--              </el-col>-->
            </el-row>
          </div>
        </el-col>
      </el-row>
      <el-row type="flex" class="row-bg" justify="end">
        <el-col :span="5">
          <el-button @click="toRegister()" type="text"><i class="el-icon-arrow-right"></i>去注册</el-button>
        </el-col>
      </el-row>
      </el-dialog>

    <el-dialog
      title="注册"
      :visible="true"
      width="600px"
      :modal="false"
      :lock-scroll="false"
      center
      id="register"
      @close="closeRegister()"
    >
      <Register/>

      <el-row type="flex" class="row-bg" justify="end">
        <el-col :span="5">
          <el-button @click="toLogin()" type="text"><i class="el-icon-arrow-right"></i>去登录</el-button>
        </el-col>
      </el-row>

    </el-dialog>
  </div>
</template>

<script>
  import {emailAndYzmLogin, nameAndPwdLogin, SHA256} from "./network/login";
  import axios from "axios";
  import Register from "./views/register/Register"
  import {addViews} from "./network/home";

  export default {
    components:{Register},
    data(){
      return{
        labelLen: '',
        activeName: 'first',
        ruleForm: {
          'name': '',
          'pwd': '',
        },
        rules: {
          name: [
            { required: true, message: '请输入用户名', trigger: 'blur' },
            {min: 1, max: 12, message: '1~12个字符', trigger: 'blur'},
          ],
          pwd: [
            { required: true, message: '请输入密码', trigger: 'blur' },
            {min: 3, max: 15, message: '3~15个字符', trigger: 'blur'},
          ],
        },
        emailForm: {
          'email': '',
          'code': '',
        },
        emailRules: {
          email: [
            { required: true, message: '请输入邮箱', trigger: 'blur' },
          ],
          code: [
            { required: true, message: '请输入验证码', trigger: 'blur' },
          ],
        },
        showCodeInput: false,
        yzm: '',
        username: '', // 记录登录成功的用户信息,
        token:'',
      }
    },
    mounted() {
      // 初始化画布
      let ele = document.getElementById('my_canvas');
      ele.width = ele.offsetWidth;
      ele.height = ele.offsetHeight;
      let ctx = ele.getContext('2d');

      // 创建圆点集合数组
      let circles=[];
      let circleCount=160;
      let currentPoint=new currentCircle(0,0);
      // 初始化circleCount个圆
      function init(){
        for(let i=0;i<circleCount;i++){
          circles.push(new circle(random(0,ele.width),random(0,ele.height)));
        }
        draw();
      }
      init();
      window.requestAnimationFrame = window.requestAnimationFrame
              || window.mozRequestAnimationFrame
              || window.webkitRequestAnimationFrame
              || window.msRequestAnimationFrame
              || function(callback){
                setInterval(callback,16.7)
              };

      function random(min,max){
        return min+Math.random()*(max-min);
      }


      // 通过调用draw()方法，实现每一次点的位置的变化（看似是在运动）
      function draw(){
        // 清空上一次的画布
        ctx.clearRect(0,0,ele.width,ele.height);

        for(let i=0;i<circles.length;i++){
          circles[i].move(ele.width,ele.height);
          circles[i].drawCircle(ctx);
          for(let j=i+1;j<circles.length;j++){
            circles[i].drawLine(ctx,circles[j]);
          }
        }
        if(currentPoint.x){
          currentPoint.drawCircle(ctx);
          for(let k=0;k<circles.length;k++){
            currentPoint.drawLine(ctx,circles[k]);
          }
        }
        requestAnimationFrame(draw);
      }
      // 鼠标移进
      window.onmousemove= function (event){
        let e = event|| window.event;
        let scrollX = document.documentElement.scrollLeft || document.body.scrollLeft;
        let scrollY = document.documentElement.scrollTop || document.body.scrollTop;
        currentPoint.x = e.pageX || e.clientX + scrollX;
        currentPoint.y= e.pageY || e.clientY + scrollY;
      }
      // 鼠标移出
      window.onmouseout = function() {
        currentPoint.x = null;
        currentPoint.y = null;
      }


      function currentCircle(x,y){
        circle.call(this,x,y)
        this.drawCircle=function (ctx){
          ctx.beginPath();
          ctx.arc(this.x,this.y,this.radius,0,360);
          ctx.fillStyle='rgba(255,255,255,0)';
          ctx.fill();
        }
      }

      // 创建圆(构造器函数)
      function circle(x,y){
        this.x=x;
        this.y=y;
        this.radius=random(.8,4);
        // 偏移
        this.speed_x=random(-1,1);
        this.speed_y=random(-1,1);

        // 移动
        this.move=function (width,height){
          this.speed_x=(this.x<width && this.x>0)?this.speed_x:(-this.speed_x);
          this.speed_y=(this.y<width && this.y>0)?this.speed_y:(-this.speed_y);
          this.x+=this.speed_x;
          this.y+=this.speed_y;
        }

        // 画圆
        this.drawCircle=function (ctx){
          ctx.beginPath();
          ctx.fillStyle=`rgba(${random(0,255)},${random(0,255)},${random(0,255)},.6)`
          ctx.arc(this.x,this.y,this.radius,0,360);
          ctx.fill();
        }

        // 画连线
        this.drawLine=function(ctx,_circle){
          let dx=this.x-_circle.x;
          let dy=this.y-_circle.y;
          let d=Math.sqrt( Math.pow(dx,2) + Math.pow(dy,2) );
          // 勾股定理求距离
          if(d<120){
            ctx.beginPath();
            ctx.moveTo(this.x,this.y);
            ctx.lineTo(_circle.x,_circle.y);
            ctx.strokeStyle='rgba(201, 201, 201, 0.5)';
            ctx.stroke();
          }
        }
      }
    },
    created() {
      axios.defaults.headers.token = localStorage.getItem("token") || ""
      addViews().then(res=>{
      }).catch(()=>{})
    },
    methods:{
      sendYzm() {
        let _this = this
        let email = this.emailForm.email;
        const emailRule = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/;
        if (!emailRule.test(email)) { // 前端先检查邮箱格式，避免恶意行为造成后端请求压力
          this.$message.error('请输入正确的邮箱格式');
        } else {
          emailAndYzmLogin(email).then(resp => {
            if (resp.data['code'] === 20000) {
              _this.$message.success("验证码发送成功，5 min 内有效")
              _this.yzm = resp.data.data['code'];
              _this.username = resp.data.data['user'];
              _this.token = resp.data.data['token'];
              _this.showCodeInput = true;
              setTimeout(()=>{
                _this.yzm = '!#$@^&*@^~_)'
              },1000*60*5)
            } else {
              this.$message.error(resp.data['message']);
            }
          }).catch(() => this.$message.error('操作失败'));
        }
      },
      return_user(){
        this.showCodeInput = false;
        document.getElementById("userLogin").style.display = 'block';
        document.getElementById("emailLogin").style.display = 'none';
      },
      showEmailLogin(){
        document.getElementById("userLogin").style.display = 'none';
        document.getElementById("emailLogin").style.display = 'block';
      },
      closeLogin(){
        document.getElementById("login").style.display = "none"
        document.getElementsByClassName("v-modal")[0].style.display = "none"
      },
      closeRegister(){
        document.getElementById("register").style.display = "none"
        document.getElementsByClassName("v-modal")[0].style.display = "none"
      },
      toRegister(){
        document.getElementById("login").style.display = "none"
        document.getElementById("register").style.display = "block"
      },
      toLogin(){
        document.getElementById("login").style.display = "block"
        document.getElementById("register").style.display = "none"
      },
      submitForm(formName) {
        let _this = this;
        this.$refs[formName].validate((valid) => {
          if (valid) {
            if (formName === 'ruleForm') {
              this.nameLogin();
              _this.$refs[formName].resetFields()
            } else {
              if (this.yzm.toLowerCase() === this.emailForm.code.toLowerCase()) {
                this.$message.success('登录成功');
                // 保存登录信息
                let userinfo = JSON.stringify(_this.username)
                localStorage.setItem('nbUser', userinfo);
                localStorage.setItem('token',_this.token)

                axios.defaults.headers.token = _this.token;
                document.getElementById("login").style.display = "none"
                document.getElementsByClassName("v-modal")[0].style.display = "none"

                _this.showCodeInput = false;
                _this.return_user()
                _this.$refs[formName].resetFields()

                let id = this.$route.params.id || ""
                if(id.length>5){
                  setTimeout(()=>{
                    location.reload();
                  },500)
                }
              } else {
                this.$message.error('验证码错误');
              }
            }
          } else {
            this.$message.error('请正确填写登录信息');
          }
        })
      },
      nameLogin() {
        nameAndPwdLogin(this.ruleForm.name, SHA256(this.ruleForm.pwd)).then(resp => {
          if (resp.data['code'] === 20000) {
            this.$message.success('登录成功');
            // 保存登录信息
            let userinfo = JSON.stringify(resp.data.data.user)
            localStorage.setItem('nbUser', userinfo);
            localStorage.setItem('token',resp.data.data.token)

            axios.defaults.headers.token = resp.data.data.token;
            document.getElementById("login").style.display = "none"
            document.getElementsByClassName("v-modal")[0].style.display = "none"

            let id = this.$route.params.id || ""
            if(id.length>5){
              setTimeout(()=>{
                location.reload();
              },500)
            }

          } else {
            this.$message.error(resp.data['message']);
          }
        }).catch(() => this.$message.error('操作失败'));
      },
    }
  }

</script>

<style>
  @import "assets/css/base.css";
  #app {
    min-height: 100vh;
    background: linear-gradient(90deg,rgba(247,149,51,.1),rgba(243,112,85,.1) 15%,rgba(239,78,123,.1) 30%,rgba(161,102,171,.1) 44%,rgba(80,115,184,.1) 58%,rgba(16,152,173,.1) 72%,rgba(7,179,155,.1) 86%,rgba(109,186,130,.1));
  }
  canvas {
    width: 100%;
    min-height: 100%;
    position: fixed;
    top: 0;
    left: 0;
  }

  .return_user{
  }

  .return_user :hover{
    color: red;
  }

  #login{
    display: none;
  }

  #register{
    display: none;
  }

  .v-modal{
    display: none;
  }

  .gap {
    border-top: 1px dashed #CCCCCC;
  }

  .myhr{
    border-bottom:1px dashed #c9c9c9;
    border-left:0;
    border-right:0;
    border-top:0;
  }

  .emailLogin-ruleForm{
    display: none;
  }
</style>
