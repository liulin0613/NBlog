<template>
  <div>
    <el-container>
      <el-header>
        <Header/>
      </el-header>
      <el-main style="z-index: 999;display: flex;justify-content: center">
        <div class="adjust_width favorite_main">
          <el-container>
            <el-aside style="width:150px">
              <Nav :activate_nav="'个人中心'"/>
            </el-aside>
            <el-main>
              <div class="fav_card">
                <el-descriptions style="padding: 20px;" class="margin-top" :labelStyle="labelStyle" title="基本信息" :column="1" :size="size" border>
                  <el-descriptions-item>
                    <template slot="label" >
                      <div class="center">
                        <i class="el-icon-picture-outline"></i> &nbsp;
                        头像
                      </div>
                    </template>
                    <el-row :gutter="24" class="center">
                      <el-col :span="6" class="center">
                        <img style="width: 80px;height: 80px;border-radius: 60px" :src="'http://nblog.org.cn/avatar/'+info.avatar+'.png'"/>
                      </el-col>
                      <el-col :span="12" :offset="6">
                        &nbsp;
                        <el-upload action=''
                                   id="upload"
                                   :on-change="getFile"
                                   :limit="1"
                                   list-type="picture"
                                   :show-file-list=false
                                   accept=".jpg,.jpeg,.png,.JPG,.JPEG"
                                   :auto-upload="false">
                          <el-button size="small" type="primary">点击上传</el-button>
                          <div slot="tip" class="el-upload__tip">只能上传 [.jpg,.jpeg,.png,.JPG,.JPEG] 文件</div>
                        </el-upload>
                      </el-col>
                    </el-row>

                  </el-descriptions-item>

                  <el-descriptions-item>
                    <template slot="label">
                      <div class="center">
                        <i class="el-icon-user"></i>&nbsp;
                        用户名
                      </div>
                    </template>
                    <el-row :gutter="24" class="center">
                      <el-col :span="6" class="center">
                        {{info.name}}
                      </el-col>
                      <el-col :span="12" :offset="6"><el-button @click="updateName()" type="primary" size="mini">修改</el-button></el-col>
                    </el-row>
                  </el-descriptions-item>

                  <el-descriptions-item>
                    <template slot="label">
                      <div class="center">
                        <svg t="1639136604705" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="3145" width="16" height="16"><path d="M512 2.56C231.424 2.56 3.584 229.888 3.584 510.976S231.424 1018.88 512 1018.88s508.416-227.328 508.416-508.416S793.088 2.56 512 2.56z m0 941.568c-239.616 0-433.664-194.048-433.664-433.664S272.384 76.8 512 76.8s433.664 194.048 433.664 433.664-194.048 433.664-433.664 433.664z" fill="#F4DCCB" p-id="3146"></path><path d="M396.288 459.776c-13.312 0-24.064-10.752-24.064-24.064v-45.056c0-77.312 62.976-140.8 140.8-140.8 77.312 0 140.8 62.976 140.8 140.8 0 13.312-10.752 24.064-24.064 24.064s-24.064-10.752-24.064-24.064c0-50.688-41.472-92.16-92.16-92.16s-92.16 41.472-92.16 92.16V435.2c-1.024 13.824-11.776 24.576-25.088 24.576z" fill="#FF9D4D" p-id="3147"></path><path d="M643.072 771.584H380.928c-32.768 0-59.904-26.624-59.904-59.904V496.128c0-32.768 26.624-59.904 59.904-59.904h262.656c32.768 0 59.904 26.624 59.904 59.904v215.04c-0.512 33.792-26.624 60.416-60.416 60.416z m-262.144-286.72c-6.144 0-11.264 5.12-11.264 11.264V711.68c0 6.144 5.12 11.264 11.264 11.264h262.656c6.656 0 11.264-5.12 11.264-12.288v-215.04c0-6.144-5.12-11.264-11.264-11.264H380.928z" fill="#FF9D4D" p-id="3148"></path><path d="M512.512 522.752c-24.064 0-44.544 19.968-44.544 44.544v12.288c0 18.432 11.776 34.816 28.16 41.472v51.712c0 8.704 7.168 16.384 16.384 16.384 8.704 0 16.384-7.168 16.384-16.384v-51.712c16.384-6.656 28.16-22.528 28.16-41.472v-12.288c0-24.064-20.48-44.544-44.544-44.544z" fill="#FF9D4D" p-id="3149"></path></svg>&nbsp;
                        密码
                      </div>
                    </template>
                    <el-row :gutter="24" class="center">
                      <el-col :span="6" class="center">
                        **************
                      </el-col>
                      <el-col :span="12" :offset="6"><el-button @click="updatePwd()" type="primary" size="mini">修改</el-button></el-col>
                    </el-row>


                    <el-dialog
                      title="修改密码"
                      :visible.sync="centerDialogVisible"
                      width="20%"
                      center>
                      <el-form ref="" :label-position="labelPosition" label-width="80px" :model="formLabelAlign">
                        <el-form-item label="旧密码">
                          <el-input show-password v-model="formLabelAlign.oPwd"></el-input>
                        </el-form-item>
                        <el-form-item label="新密码">
                          <el-input show-password v-model="formLabelAlign.nPwd"></el-input>
                        </el-form-item>
                        <el-form-item label="再次输入">
                          <el-input show-password v-model="formLabelAlign.rePwd"></el-input>
                        </el-form-item>
                      </el-form>
                      <span slot="footer" class="dialog-footer">
                        <el-button @click="cancel()">取 消</el-button>
                        <el-button type="primary" @click="submitUpdatePwd()">确 定</el-button>
                      </span>
                    </el-dialog>
                  </el-descriptions-item>

                  <el-descriptions-item>
                    <template slot="label">
                      <div class="center">
                        <svg t="1639136696561" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="3612" width="16" height="16"><path d="M843.875 135.125h-90V90.125c0-11.25-16.875-28.125-33.75-28.125s-28.125 11.25-28.125 28.125v39.375H360.125V90.125c0-11.25-11.25-28.125-28.125-28.125s-28.125 16.875-28.125 28.125v39.375H180.125C129.5 135.125 90.125 174.5 90.125 219.5v652.5c0 50.625 39.375 90 90 90h663.75c50.625 0 90-39.375 90-90V219.5c0-45-39.375-84.375-90-84.375z m28.125 736.875c0 16.875-11.25 28.125-28.125 28.125H180.125c-16.875 0-28.125-11.25-28.125-28.125V219.5c0-16.875 11.25-28.125 28.125-28.125h118.125v5.625c5.625 16.875 16.875 33.75 33.75 33.75s28.125-11.25 28.125-28.125v-11.25h331.875v5.625c0 16.875 11.25 28.125 28.125 28.125s28.125-11.25 28.125-28.125v-5.625h90c16.875 0 28.125 11.25 28.125 28.125v652.5z" p-id="3613"></path><path d="M753.875 337.625H270.125c-16.875 0-28.125 11.25-28.125 28.125s11.25 33.75 28.125 33.75h483.75c16.875 0 28.125-11.25 28.125-28.125s-11.25-33.75-28.125-33.75zM753.875 517.625H270.125c-16.875 0-28.125 11.25-28.125 28.125s11.25 28.125 28.125 28.125h483.75c16.875 0 28.125-11.25 28.125-28.125s-11.25-28.125-28.125-28.125zM512 697.625H270.125c-16.875 0-28.125 11.25-28.125 28.125s11.25 28.125 28.125 28.125H512c16.875 0 28.125-11.25 28.125-28.125s-11.25-28.125-28.125-28.125z" p-id="3614"></path></svg>&nbsp;
                        描述
                      </div>
                    </template>
                    <el-row :gutter="24" class="center">
                      <el-col :span="6" class="center">
                        {{info.intro}}
                      </el-col>
                      <el-col :span="12" :offset="6"><el-button @click="updateIntro()" type="primary" size="mini">修改</el-button></el-col>
                    </el-row>
                  </el-descriptions-item>

                  <el-descriptions-item>
                    <template slot="label">
                      <div class="center">
                        <svg t="1639136499816" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="2342" width="16" height="16"><path d="M838.954667 234.666667H170.666667c-3.626667 0-7.168 0.448-10.56 1.322666l323.690666 323.669334a21.333333 21.333333 0 0 0 30.165334 0L838.954667 234.666667z m46.144 14.186666l-260.693334 260.693334 262.933334 262.912c5.44-7.168 8.661333-16.106667 8.661333-25.792V277.333333c0-10.944-4.117333-20.906667-10.88-28.48zM843.861333 789.333333l-249.6-249.621333-50.133333 50.133333a64 64 0 0 1-90.517333 0l-50.112-50.133333L156.373333 786.88c4.48 1.578667 9.28 2.453333 14.314667 2.453333h673.194667zM128.661333 754.218667L373.333333 509.525333 129.578667 265.813333A42.709333 42.709333 0 0 0 128 277.333333v469.333334c0 2.56 0.213333 5.098667 0.661333 7.552zM170.666667 192h682.666666a85.333333 85.333333 0 0 1 85.333334 85.333333v469.333334a85.333333 85.333333 0 0 1-85.333334 85.333333H170.666667a85.333333 85.333333 0 0 1-85.333334-85.333333V277.333333a85.333333 85.333333 0 0 1 85.333334-85.333333z" fill="#333333" p-id="2343"></path></svg>&nbsp;
                        邮箱
                      </div>
                    </template>
                    <el-row :gutter="24" class="center">
                      <el-col :span="6" class="center">
                        {{info.email}}
                      </el-col>
                      <el-col :span="12" :offset="6">&nbsp;</el-col>
                    </el-row>
                  </el-descriptions-item>

                  <el-descriptions-item>
                    <template slot="label" >
                      <div class="center">
                        <i class="el-icon-time"></i> &nbsp;
                        注册时间
                      </div>
                    </template>
                    <el-row :gutter="24" class="center">
                      <el-col :span="6" class="center">
                        {{info.createTime.substring(0,11)}}
                      </el-col>
                      <el-col :span="12" :offset="6">&nbsp;</el-col>
                    </el-row>

                  </el-descriptions-item>
                </el-descriptions>
                <br>
                <el-descriptions style="padding: 20px;"  :colon=false :labelStyle="labelStyle2" :column="1" title="社交信息">

                  <el-descriptions-item label="所在地 : ">{{socialInfo.location}}</el-descriptions-item>

                  <el-descriptions-item label="我的关注 : ">
                    <div class="center">
                      {{socialInfo.attCount}} 人 &nbsp;&nbsp;
                      <el-button @click="showAtt = !showAtt" size="small" type="text">点击隐藏</el-button>
                    </div>

                  </el-descriptions-item>

                  <el-descriptions-item label="">

                    <div v-if="showAtt">
                      <div  @click="see(item.id)" v-for="(item,index) in socialInfo.att" class="image-content">
                        <img :src="'http://nblog.org.cn/avatar/'+item.avatar+'.png'" />
                        <p>{{item.uname}}</p>
                      </div>
                    </div>


                  </el-descriptions-item>

                  <el-descriptions-item label="我的粉丝  : ">
                    {{socialInfo.fansCount}} 人 &nbsp;&nbsp;
                    <el-button @click="showFans = !showFans" size="small" type="text">点击隐藏</el-button>
                  </el-descriptions-item>

                  <el-descriptions-item label="" >
                    <div v-if="showFans">
                      <div  @click="see(item.id)" v-for="(item,index) in socialInfo.fans" class="image-content">
                        <img :src="'http://nblog.org.cn/avatar/'+item.avatar+'.png'" />
                        <p>{{item.uname}}</p>
                      </div>
                    </div>
                  </el-descriptions-item>

                </el-descriptions>
              </div>
            </el-main>
          </el-container>
        </div>
      </el-main>
    </el-container>
  </div>
</template>

<script>

  import Nav from "../nav/Nav";
  import Header from "../../components/header/Header";
  import {alertAvatar, getSocialInfo, updateUserIntro, updateUserName, updateUserPwd} from "../../network/info";
  import {SHA256} from "../../network/login";
  let loginCheck;

  export default {
    name: "Favorite",
    components:{Nav,Header},
    data(){
      return{
        labelPosition: 'right',
        formLabelAlign: {
          oPwd: '',
          nPwd: '',
          rePwd: ''
        },
        showAtt:true,
        showFans:true,
        centerDialogVisible:false,
        info:[],
        size: '',
        socialInfo:{att:[{'avatar':'default_avatar'}],fans:[{'avatar':'default_avatar'}]},
        labelStyle:{width:'150px'},
        labelStyle2:{width: '80px','align-items':'center',display:'flex','justify-content':'center','font-weight':'bold'}
      }
    },
    methods:{
      cancel(){
        this.centerDialogVisible = false
        this.formLabelAlign={
          oPwd: '',
          nPwd: '',
          rePwd: ''
        }
      },
      see(id){
        localStorage.setItem("currentList",id)
        this.$router.push("/nblog/list")
      },
      submitUpdatePwd(){
        if(SHA256(this.formLabelAlign.oPwd) !== this.info.pwd){
          this.$message.error("旧密码错误");
        }else if(this.formLabelAlign.nPwd !== this.formLabelAlign.rePwd){
          this.$message.error("两次密码不一致");
        }else if(this.formLabelAlign.nPwd.length<3 || this.formLabelAlign.nPwd.length>15){
          this.$message.error("密码长度为 3-15");
        }else if(this.formLabelAlign.oPwd === this.formLabelAlign.nPwd){
          this.$message.error("新旧密码不能相同");
        }else {
          updateUserPwd(SHA256(this.formLabelAlign.nPwd)).then(res =>{
            if(res.data.code === 20000){
              this.info.pwd = SHA256(this.formLabelAlign.nPwd)
              localStorage.setItem("nbUser",JSON.stringify(this.info))
              this.$message.success("修改成功")
              this.centerDialogVisible = false

              this.formLabelAlign={
                oPwd: '',
                nPwd: '',
                rePwd: ''
              }

            }else {
              this.$message.error(res.data.message);
            }
          }).catch(()=>{})
        }
      },
      updateName(){
        let _this = this
        this.$prompt('新名字，新气象~', '输入新名字', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          inputPattern:/^[_0-9a-zA-Z\u4e00-\u9fa5]{1,12}$/,
          inputErrorMessage: '1~12个字符，只支持字母，数字和下划线'
        }).then(({value}) => {
          if(value===this.info.name){
            this.$message.success("修改成功")
          }else {
            updateUserName(value).then(res => {
              if(res.data.code === 20000){
                _this.info.name = value
                localStorage.setItem("nbUser",JSON.stringify(_this.info))
                _this.$message.success("修改成功")
              }else{
                _this.$message.error(res.data.message)
              }
            })
          }
        }).catch(() => {});
      },
      updateIntro(){
        this.$prompt('介绍一下自己，让别人更容易记住你~', '输入简介', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          inputPattern:/^[\s\S]{1,30}$/,
          inputErrorMessage: '1-30 个字符'
        }).then(({value}) => {
          if (value === this.info.intro) {
            this.$message.success("修改成功")
          } else {
            updateUserIntro(value).then(res=>{
              if(res.data.code===20000){
                this.info.intro = value;
                localStorage.setItem("nbUser",JSON.stringify(this.info))
                this.$message.success("修改成功")
              }else {
                this.$message.error(res.data.message)
              }
            })

          }
        }).catch(() => {});
      },
      updatePwd(){
        this.centerDialogVisible = true;
      },
      getFile(file) {
        this.getBase64(file.raw).then(res => {
          this.base64ThumbImage(res, 840, 840);
        });
      },
      base64ThumbImage(elementObj,maxWidth,maxHeight){
        let _localStorage = localStorage
        let _this = this
        if (typeof (FileReader) === 'undefined') {
          this.$message("抱歉，你的浏览器不支持发送图片，请升级浏览器或切换浏览器再试！");
        } else {
          try {
            //创建一个img对象
            const img = new Image();
            img.src = elementObj

            // base64地址图片加载完毕后执行
            img.onload = function () {
              // 缩放图片需要的canvas（也可以在DOM中直接定义canvas标签，这样就能把压缩完的图片不转base64也能直接显示出来）
              const canvas = document.createElement('canvas');
              const context = canvas.getContext('2d');
              // 图片原始尺寸
              const originWidth = this.width;
              const originHeight = this.height;
              // 最大尺寸限制，可通过设置宽高来实现图片压缩程度
              //var maxWidth = 500;
              //var maxHeight = 400;
              // 目标尺寸
              let targetWidth = originWidth, targetHeight = originHeight;
              // 图片尺寸超过最大值的限制
              if (originWidth > maxWidth || originHeight > maxHeight) {
                if (originWidth / originHeight > maxWidth / maxHeight) {
                  // 更宽，按照宽度限定尺寸
                  targetWidth = maxWidth;
                  targetHeight = Math.round(maxWidth * (originHeight / originWidth));
                } else {
                  targetHeight = maxHeight;
                  targetWidth = Math.round(maxHeight * (originWidth / originHeight));
                }
              }
              // canvas对图片进行缩放
              canvas.width = targetWidth;
              canvas.height = targetHeight;
              // 清除画布
              context.clearRect(0, 0, targetWidth, targetHeight);
              // 图片压缩
              /*第一个参数是创建的img对象；第二三个参数是左上角坐标，后面两个是画布区域宽高*/
              context.drawImage(img, 0, 0, targetWidth, targetHeight);
              //压缩后的图片转base64 url
              //canvas.toDataURL(mimeType, qualityArgument),mimeType 默认值是'image/png';
              //qualityArgument表示导出的图片质量，只有导出为jpeg和webp格式的时候此参数才有效，默认值是0.92
              //base64 格式
              let compressionImg = canvas.toDataURL('image/jpeg', 0.92)

              alertAvatar(compressionImg).then(res=>{
                if(res.data.code === 20000){
                  _this.info.avatar = res.data.data
                  _localStorage.setItem("nbUser",JSON.stringify(_this.info))
                  _this.$message.success("修改成功")
                  document.getElementById("upload").style.display='none'
                }
              }).catch(()=>{})

            }

          } catch (e) {
          }
        }
      },
      getBase64(file) {
        return new Promise(function(resolve, reject) {
          let reader = new FileReader();
          let imgResult = "";
          reader.readAsDataURL(file);
          reader.onload = function() {
            imgResult = reader.result;
          };
          reader.onerror = function(error) {
            reject(error);
          };
          reader.onloadend = function() {
            resolve(imgResult);
          };
        });
      },
    },
    created() {
      this.info = JSON.parse(localStorage.getItem("nbUser"))
      getSocialInfo().then(res=>{
        if(res.data.code === 20000){
          this.socialInfo = res.data.data
        }
      }).catch(()=>{})
    },
    mounted(){
      let _this = this
      loginCheck = setInterval(()=>{
        let token = localStorage.getItem("token") || ""
        if(token.length<5){
          _this.$message.info("登录已退出")
          _this.$router.push('/')
        }
      },100)
    },
    destroyed(){
      clearInterval(loginCheck)
    }
  }
</script>

<style scoped>
  .image-content{
    text-align: center;
    float: left;
    padding-left: 20px;
  }
  .image-content img{
    width: 70px;height: 70px;
    border-radius: 60px;
  }
  .center{
    display: flex;
    justify-content: center;
    align-items: center;
  }
  .favorite_main{
    width: 1500px;
    padding: 0 120px 0 120px;
    position: relative;
    z-index: 100;
  }

  .fav_card{
    height: 79vh;
    width: 100%;
    background-color: #fff;
    overflow: scroll;
    -ms-overflow-style: none;
    scrollbar-width: none; /* Firefox */
  }
  .fav_card::-webkit-scrollbar {
    display: none; /* Chrome Safari */
  }

  .fav_card::-webkit-scrollbar {
    display: none; /* Chrome Safari */
  }
</style>
