<template>
  <div>
    <el-row style="min-height: 400px">
      <div class="shade">&nbsp;
      </div>

      <div class="removesha">
        <div class="article-info-container">
          <div class="article-title">标签 - {{tagName}}</div>
          <div class="article-info">
            <div class="first-line">
              <span>标签贡献者 TOP 3 ：</span>
              <span v-for="item in contribution">
                {{item.name}} -- {{item.value}} &nbsp;&nbsp;
              </span>
              &nbsp;</div>
            <div class="second-line">
              &nbsp;
            </div>
          </div>
        </div>
      </div>

      <div class="banner">
        &nbsp;
      </div>
    </el-row>

    <el-row class="tag_info">
      <el-col :span="2">&nbsp;</el-col>
      <el-col :span="20">
        <el-row>
          <el-col  style="padding: 10px; height: 500px;width: 400px" v-for="(item,index) in articles" :key="index" :span="8">
            <div class="tag_info" @click="skip(item)">

              <div @click="look(item)" class="info_image_01" v-if="item.img.length>0">
                <img style="max-width: 100%;max-height: 100%" :src="'http://nblog.org.cn/images/'+item.userId+'/'+item.img+'.png'"/>
              </div>
              <div v-else  class="info_image">
                <img  :src="'http://nblog.org.cn/images/sys/t'+random()+'.jpg'" />
              </div>
              <div class="title">
                <span>{{item.title}}</span>
                <br><br>
              </div>
              <div class="info_time">
                <span><i class="el-icon-time"></i> {{item.createTime.substring(0,10)}}</span>
                &nbsp;&nbsp;&nbsp;
                <span><i class="el-icon-user-solid"></i>&nbsp;&nbsp;{{item.author}}</span>
                <br><br>
                <div style="height: 1px;background-color: #000; width: 100%;"></div>
              </div>
              <div class="info_tag">
                <span class="tag" v-for="tag in item.tags.split(',').slice(0,3)">
                  {{tag}}
                </span>
              </div>
            </div>
          </el-col>
        </el-row>
      </el-col>
    </el-row>
  </div>
</template>

<script>
    import {getTagDetails} from "../../network/tag";

    export default {
        name: "Tag",
      data(){
          return{
            tagName:'',
            articles:[],
            contribution:[],
          }
      },
      computed:{

      },
      created() {
        this.tagName = this.$route.params.tag;
        getTagDetails(this.tagName).then(resp => {
          this.articles  = resp.data['data'];
          let contri = [];
          for (let i = 0; i < this.articles.length; i++) {
            let author = this.articles[i].author
            if(contri[author]===undefined){
              contri[author]=1
            }else {
              contri[author] = contri[author]+1
            }
          }
          let real_contri = []
          for (let key in contri) {
            let item = contri[key];
            real_contri.push({name:key,value:item})
          }

          var compare = function (obj1, obj2) {
            var val1 = obj1.value;
            var val2 = obj2.value;
            if (val1 < val2) {
              return 1;
            } else if (val1 > val2) {
              return -1;
            } else {
              return 0;
            }
          }

          real_contri= real_contri.sort(compare);

          if(real_contri.length > 3){
            this.contribution.push(real_contri[0]);
            this.contribution.push(real_contri[1]);
            this.contribution.push(real_contri[2]);
          }else{
            this.contribution = real_contri
          }
        }).catch(() => {});
      },
      methods:{
        skip(item){
          const encode=(item.id*99)^0xBEEF;
          this.$router.push("/nblog/article/"+encode+".html")
        },
        random(){
          return Math.floor(Math.random() * 27);
        },
      }
    }
</script>

<style scoped>
  .info_image_01{
    background:#fff;width: 100%;height: 300px; display: flex;align-items:center;justify-content: center;
    border-top-left-radius: 10px;
    border-top-right-radius: 10px;
  }
  .title{
    height: 50px;
    font-size: 15px;
    padding: 15px 10px 2px 10px;
    background: #fff;
    color: #000;
  }

  .info_time{
    height: 30px;
    background: #fff;
    padding: 2px 10px 5px 10px;
  }

  .title:hover{
    color: red;
  }

  .content{
    height: 230px;font-size: 16px;
    margin-bottom: 20px;
  }

  .content img{
    width: 100%;
    height: 90%;
  }

  .el-col-8 :hover{
  }

  .tag{
    -webkit-text-size-adjust: 100%;
    text-rendering: optimizeLegibility;
    -webkit-font-smoothing: antialiased;
    -webkit-tap-highlight-color: rgba(0,0,0,0);
    overflow-wrap: break-word;
    white-space: normal;
    box-sizing: border-box;
    font-family: -apple-system,BlinkMacSystemFont,Segoe UI,Helvetica Neue,Lato,Roboto,PingFang SC,Microsoft YaHei,sans-serif!important;
    display: inline-block;
    font-size: 0.725rem;
    height: 22px;
    border-radius: 10px;
    background: linear-gradient(90deg, rgb(191, 70, 67) 0px, rgb(108, 157, 143));
    opacity: 0.6;
    margin: 0 0.5rem 10px 0;
    padding: 2px 12px !important;
    text-decoration: none;
    transition: all 0.3s ease 0s;
    color: rgb(255, 255, 255) !important;
  }
  .banner{
    -webkit-text-size-adjust: 100%;
    font-size: 16px;
    text-rendering: optimizeLegibility;
    -webkit-font-smoothing: antialiased;
    -webkit-tap-highlight-color: rgba(0,0,0,0);
    line-height: 1.5;
    padding: 0;
    margin: 0;
    text-decoration: none;
    box-sizing: border-box;
    font-family: -apple-system,BlinkMacSystemFont,Segoe UI,Helvetica Neue,Lato,Roboto,PingFang SC,Microsoft YaHei,sans-serif!important;
    position: absolute;
    background-color: #49b1f5!important;
    left: 0;
    right: 0;
    height: 400px;
    animation: header-effect 1s;
    color: #eee!important;
    background: url("../../assets/img/tagBg.png") center center / cover no-repeat;
    z-index: 200;
  }

  .removesha{
    background-color:inherit;
    z-index: 203;
  }

  .shade{
    position: absolute;
    left: 0;
    right: 0;
    height: 400px;
    width: 100%;
    background-color: black;
    opacity: .3;
    z-index: 201;
  }

  .article-info-container{
    -webkit-text-size-adjust: 100%;
    font-size: 16px;
    text-rendering: optimizeLegibility;
    -webkit-font-smoothing: antialiased;
    -webkit-tap-highlight-color: rgba(0,0,0,0);
    line-height: 1.5;
    color: #eee!important;
    background-repeat: no-repeat;
    margin: 0;
    text-decoration: none;
    box-sizing: border-box;
    font-family: -apple-system,BlinkMacSystemFont,Segoe UI,Helvetica Neue,Lato,Roboto,PingFang SC,Microsoft YaHei,sans-serif!important;
    position: absolute;
    bottom: 6.25rem;
    padding: 0 8%;
    width: 100%;
    text-align: center;
    z-index: 202;
  }

  .article-title{
    -webkit-text-size-adjust: 100%;
    text-rendering: optimizeLegibility;
    -webkit-font-smoothing: antialiased;
    -webkit-tap-highlight-color: rgba(0,0,0,0);
    line-height: 1.5;
    color: #eee!important;
    text-align: center;
    background-repeat: no-repeat;
    padding: 0;
    text-decoration: none;
    box-sizing: border-box;
    font-family: -apple-system,BlinkMacSystemFont,Segoe UI,Helvetica Neue,Lato,Roboto,PingFang SC,Microsoft YaHei,sans-serif!important;
    font-size: 35px;
    margin: 20px 0 8px;
  }

  .article-info{
    -webkit-text-size-adjust: 100%;
    text-rendering: optimizeLegibility;
    -webkit-font-smoothing: antialiased;
    -webkit-tap-highlight-color: rgba(0,0,0,0);
    color: #eee!important;
    text-align: center;
    background-repeat: no-repeat;
    padding: 0;
    margin: 0;
    text-decoration: none;
    box-sizing: border-box;
    font-family: -apple-system,BlinkMacSystemFont,Segoe UI,Helvetica Neue,Lato,Roboto,PingFang SC,Microsoft YaHei,sans-serif!important;
    font-size: 14px;
    line-height: 1.9;
    display: inline-block;
  }

  .first-line{
    -webkit-text-size-adjust: 100%;
    text-rendering: optimizeLegibility;
    -webkit-font-smoothing: antialiased;
    -webkit-tap-highlight-color: rgba(0,0,0,0);
    color: #eee!important;
    text-align: center;
    font-size: 14px;
    line-height: 1.9;
    background-repeat: no-repeat;
    padding: 0;
    margin: 0;
    text-decoration: none;
    box-sizing: border-box;
    font-family: -apple-system,BlinkMacSystemFont,Segoe UI,Helvetica Neue,Lato,Roboto,PingFang SC,Microsoft YaHei,sans-serif!important;
  }

  .tag_info{
    padding: 15px;
    position: relative;
    z-index: 999;
  }

  .info_image{
    height: 300px;
    width: 100%;
    border-top-left-radius: 10px;
    border-top-right-radius: 10px;
  }

  .info_image img{
    width: 100%;
    height: 100%;
    border-top-left-radius: 10px;
    border-top-right-radius: 10px;
  }


  .tagsd .el-card__body{
    padding: 20px 0;
  }

  .info_tag{
    background: #fff;
    height: 50px;
    padding: 15px 15px 0 15px;
    border-bottom-left-radius: 10px;
    border-bottom-right-radius: 10px;
  }
</style>
