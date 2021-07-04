<template>
  <div class="box-card">
    <el-card>
      <div id="thisYearLoading"><i class="el-icon-loading"></i> 加载中...</div>
      <div id="thisYear" :style="{width: '350px', height: '300px'}"></div>
    </el-card>
    <br><br>
    <el-card>
      <div id="recentYearsLoading"><i class="el-icon-loading"></i> 加载中...</div>
      <div id="recentYears" style="width: 350px;height:300px;"></div>
    </el-card>
  </div>
</template>

<script>
  import echarts from 'echarts'
  import {getEchartsData} from "network/published";

  export default({
    name: "PublishedECharts",
    data () {
      return {
        thisYear: [],
        recentYears:[
          { value: 0, name: "0" },
          { value: 0, name: "0" },
          { value: 0, name: "0" },
        ]
      }
    },
    created(){
      getEchartsData(this.$store.state.user)
        .then(res =>{
          this.thisYear=res.data.extend.monthCounts
          const date = new Date();
          let year=date .getFullYear()
          for(let i=0;i<3;i++){
            this.recentYears[i].value=res.data.extend.yearCounts[i]
            this.recentYears[i].name=year-i+''
          }

        })
        .catch(err => {})
    },
    mounted(){
      setTimeout(()=>{
        this.drawBar();
        this.drawPie();
      },1000)
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
  .box-card{
    position: fixed;
    width: 350px;
  }
</style>
