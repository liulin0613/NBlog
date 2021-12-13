<template>
  <div class="box-card">
    <el-row type="flex" >
      <el-col :span="6">
        <div>
          <div id="thisYearLoading"><i class="el-icon-loading"></i> 加载中...</div>
          <div id="thisYear" :style="{width: '450px', height: '400px'}"></div>
        </div>
      </el-col>
      <el-col :span="6">&nbsp;</el-col>
      <el-col :span="6">
        <div>
          <div id="recentYearsLoading"><i class="el-icon-loading"></i> 加载中...</div>
          <div id="recentYears" style="width: 450px;height:400px;"></div>
        </div>
      </el-col>
    </el-row>


    <br><br>
    <hr>
    <br><br>

    <el-card>
      <h1 slot="header">信息统计</h1>
      <el-form ref="form" label-width="200px">
        <el-form-item style="font-weight: bold" label="共发表文章 ：">
          <span style="font-weight: normal">{{info.count}} 篇，其中，【 <span style="color: #880000">{{info.title}} </span> 】人气最高，共获得 【 {{info.total}} 】 访问量</span>
        </el-form-item>
      </el-form>
    </el-card>

  </div>
</template>

<script>
  import echarts from 'echarts'
  import {getArticleInfo, getEchartsData} from "../../network/publish";

  export default({
    name: "PublishedECharts",
    data () {
      return {
        thisYear: [],
        recentYears:[
          { value: 0, name: "0" },
          { value: 0, name: "0" },
          { value: 0, name: "0" },
        ],
        info:[]
      }
    },
    created(){
      getEchartsData()
        .then(res =>{
          this.thisYear=res.data.data.monthCounts
          const date = new Date();
          let year=date .getFullYear()
          for(let i=0;i<3;i++){
            this.recentYears[i].value=res.data.data.yearCounts[i]
            this.recentYears[i].name=year-i+''
          }
        })
        .catch(err => {})

      getArticleInfo().then(res =>{
        this.info = res.data.data
      })
    },
    mounted(){
      let timer = null;
      let that = this

      timer = setInterval(function(){
        if(that.thisYear.length !== 0){
          that.drawBar();
          that.drawPie();
          clearInterval(timer);
        }
      },100);
    },
    methods: {
      drawBar() {
        document.getElementById('thisYearLoading').style.display='none'
        const date = new Date();
        let year=date .getFullYear()
        let myChart = echarts.init(document.getElementById('thisYear'))
        myChart.setOption({
          title: { text: year+' 年发文统计' },
          tooltip: {},
          xAxis: {
            data: ["1月", "2月", "3月", "4月", "5月", "6月","7月", "8月", "9月", "10月", "11月", "12月"]
          },
          yAxis: {},
          series: [{
            name: '数量',
            type: 'bar',
            data: this.thisYear
          }],
          color: ["#7EC0EE", "#FF9F7F", "#FFD700", "#C9C9C9", "#E066FF", "#C0FF3E"]
        });
      },
      drawPie(){
        document.getElementById('recentYearsLoading').style.display='none'
        let myChart = echarts.init(document.getElementById('recentYears'));
        const date = new Date();
        let year=date .getFullYear()
        myChart.setOption({
          title: { text: '近三年发文统计' },
          tooltip: {},
          legend: {
            orient: "vertical",
            left:0,
            top: 50,
            data: [year+'', year-1+'', year-2+'']
          },
          series: [
            {
              name: "数量",
              type: "pie",
              radius: "55%",
              center: ["50%", "60%"],
              data: this.recentYears,
              emphasis: {
                itemStyle: {
                  shadowBlur: 10,
                  shadowOffsetX: 0,
                  shadowColor: "rgba(0, 0, 0, 0.5)"
                }
              },
            }
          ]
        })
      }

    }
  });
</script>

<style scoped>
  .el-form-item{
    margin-bottom: 4px;
  }

  .el-form-item__label{
    font-weight: bold;
  }

  .box-card{
    min-width:600px;
  }

</style>
