<template>
  <div>
    <el-container>
      <el-header>
        <Header/>
      </el-header>
      <el-main style="display: flex; justify-content: center">
        <div  class="adjust_width tags_main">
          <el-container>
            <el-aside style="width:150px">
              <Nav :activate_nav="'标签'"/>
            </el-aside>
            <el-main class="echart_main">
              <el-row>
                <el-card style="width: 40vh">
                  <span>统计方式 ：&nbsp;</span>
                  <el-radio-group v-model="mymode">
                    <el-radio  label="top" >Top 20</el-radio>
                    <el-radio  label="all">All</el-radio>
                  </el-radio-group>
                </el-card>
              </el-row>
              <div :class="mymode==='top'?'showdiv':'cdiv'" id="echart"></div>
              <div :class="mymode==='all'?'showdiv':'cdiv'">
                <el-card class="showAll">
                  <el-timeline>
                    <el-timeline-item
                      v-for="(value,key,index) in formatTags"
                      :key="index"
                      size="large">
                      <el-row class="mz">
                        <el-col :span="1">
                          <el-avatar size="small"> {{key}} </el-avatar>
                        </el-col>
                        <el-col :span="22">
                          <div class="tag-group">
                            <el-tag
                              v-for="(item,i) in value"
                              :key="i"
                              :type="type[(i+random())%5]"
                              effect="dark">
                              <a href="javascript:;" @click="skip(item)">{{item.name}} ({{item.count}})</a>
                            </el-tag>
                          </div>
                        </el-col>
                      </el-row>
                    </el-timeline-item>
                  </el-timeline>
                </el-card>
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
  import {getAllTags} from "../../network/tag";
  import * as echarts from 'echarts';
  const pinyin = require('js-pinyin');

    export default {
      name: "AllTags",
      components:{Nav,Header},
      data(){
          return{
            tags:[],
            mymode:"top",
            type:['success','','warning','danger','info'],
          }
      },
      computed:{
        formatTags(){
          let result={};
          for(let i=0;i<this.tags.length;i++){
            var letter=pinyin.getCamelChars(this.tags[i].tagName.trim().toUpperCase()[0]).toLocaleString();
            if(result[letter]===undefined){
              result[letter]=[{"name":this.tags[i].tagName,"count":this.tags[i].tagCount}]
            }else
              result[letter].push({"name":this.tags[i].tagName,"count":this.tags[i].tagCount})
          }

          var newkey = Object.keys(result).sort();
          var newObj = {};//创建一个新的对象，用于存放排好序的键值对
          for (var i = 0; i < newkey.length; i++) {//遍历newkey数组
            newObj[newkey[i]] = result[newkey[i]];//向新创建的对象中按照排好的顺序依次增加键值对
          }
          return newObj;
        },
      },
      created() {
        document.documentElement.scrollTop = 0
      },
      methods:{
        getAllTags(){
          let _this = this
          getAllTags().then(res => {
            this.tags = res.data.data

            let center_tags = [];
            let aside_tags = [];
            let legend_data=[];

            for(let i=0;i<this.tags.length;i++){
              if(i<3){
                center_tags.push({value:this.tags[i].tagCount,name:this.tags[i].tagName})
                legend_data.push(this.tags[i].tagName)
              }else if(i<20) {
                aside_tags.push({value:this.tags[i].tagCount,name:this.tags[i].tagName})
                legend_data.push(this.tags[i].tagName)
              }
            }

            let chartDom = document.getElementById('echart');
            let myChart = echarts.init(chartDom);
            let option;

            option = {
              tooltip: {
                trigger: 'item',
                formatter: '{a} <br/>{b}: {c} ({d}%)'
              },
              legend: {
                data: legend_data
              },
              series: [
                {
                  name: '标签',
                  type: 'pie',
                  selectedMode: 'single',
                  radius: [0, '30%'],
                  label: {
                    position: 'inner',
                    fontSize: 14
                  },
                  labelLine: {
                    show: false
                  },
                  data: center_tags
                },
                {
                  name: '标签',
                  type: 'pie',
                  radius: ['45%', '60%'],
                  labelLine: {
                    length: 30
                  },
                  label: {
                    formatter: '{a|{a}}{abg|}\n{hr|}\n  {b|{b}：}{c}  {per|{d}%}  ',
                    backgroundColor: '#F6F8FC',
                    borderColor: '#8C8D8E',
                    borderWidth: 1,
                    borderRadius: 4,
                    rich: {
                      a: {
                        color: '#6E7079',
                        lineHeight: 22,
                        align: 'center'
                      },
                      hr: {
                        borderColor: '#8C8D8E',
                        width: '100%',
                        borderWidth: 1,
                        height: 0
                      },
                      b: {
                        color: '#4C5058',
                        fontSize: 14,
                        fontWeight: 'bold',
                        lineHeight: 33
                      },
                      per: {
                        color: '#fff',
                        backgroundColor: '#4C5058',
                        padding: [3, 4],
                        borderRadius: 4
                      }
                    }
                  },
                  data: aside_tags
                }
              ]
            };
            option && myChart.setOption(option);

            myChart.on('click',  function(param) {
              _this.$router.push('/nblog/tag/'+param.name)
            });

          }).catch(()=>{})
        },
        random(){
          return Math.floor(Math.random() * 5);
        },
        skip(item){
          this.$router.push('/nblog/tag/'+item.name)
        }
      },
      mounted() {
        this.getAllTags();
      }
    }
</script>

<style scoped>
  .el-header{
    padding: 0;
  }

  .tags_main{
    padding: 0 120px 0 120px;
    position: relative;
    z-index: 100;
    width: 1500px;
  }

  #echart{
    width: 100%;
    height: 100%;
    margin-top: 20px;
    padding-top: 20px;
  }

  .showAll{
    width: 100%;
    margin-top: 20px;
    padding-top: 20px;
    background-color: inherit;
    box-shadow: inherit;
  }

  .echart_main{
    width: 100%;
    min-height: 70vh;
    overflow: hidden;
  }

  .showdiv{
    display: block;
  }

  .cdiv{
    display: none;
  }

  .tag-group span{
    margin: 0 0 5px 5px;

  }
</style>
