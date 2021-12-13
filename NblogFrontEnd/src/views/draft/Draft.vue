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
              <Nav :activate_nav="'草稿箱'"/>
            </el-aside>
            <el-main>
              <div class="draft_card">
                <div class="draftEmpty" v-if="drafts.length === 0"></div>
                <div v-else class="drafts">
                  <el-timeline>
                    <el-timeline-item v-for="(item,i) in drafts" :timestamp="item.updateTime.substring(0,11)" placement="top">
                      <el-card style="width: 70%;">
                        <h1>
                        <span>
                          {{item.title}}
                        </span>
                        </h1>
                        <br>
                        <div>
                          <el-button type="text">操作：</el-button>
                          &nbsp;
                          <el-button @click="editor(item)" slot="reference"  type="primary" size="mini" icon="el-icon-edit" round>编辑</el-button>
                          &nbsp;&nbsp;
                          <el-popconfirm :title="'确定删除草稿 -- '+item.title" @confirm="deleteDrafts(item.id,i)">
                            <el-button slot="reference" type="danger" size="mini" icon="el-icon-delete" round> 删除</el-button>
                          </el-popconfirm>
                        </div>
                      </el-card>
                    </el-timeline-item>
                  </el-timeline>
                </div>
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
  import {deleteDraft, getAllDraft} from "../../network/draft";
  let  loginCheck;
  export default {
    name: "Draft",
    components: {Nav, Header},
    data(){
      return{
        drafts:[]
      }
    },
    created(){
      getAllDraft().then(res=>{
        if(res.data.code === 20000){
          this.drafts = res.data.data
        }
      })
    },
    methods:{
      editor(item){
        let editorStatus = localStorage.getItem("editorStatus") || "";
        if(editorStatus.length>2){
          this.$confirm('当前有文章正在编辑，继续会覆盖编辑栏内容, 是否继续?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            this.realEditor(item)
          }).catch(() => {});
        }else{
          this.realEditor(item)
        }
      },

      realEditor(item){
        localStorage.setItem("editorStatus","reDraft")
        localStorage.setItem("needLoad","true")
        localStorage.setItem("draftID",item.id)

        deleteDraft(item.id)
          .then(res => {
            if(res.data.code === 20000){
              this.$router.push('/nblog/publish')
            }
          })
          .catch(()=>{})
      },

      deleteDrafts(did,i){
        deleteDraft(did)
          .then(res => {
            this.$message({
              type:"success",
              message:"删除--"+this.drafts[i].title+"--成功"
            })
            this.drafts.splice(i,1)
          })
          .catch(err => this.$message.error("删除失败"))
      },
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

  .drafts{
    padding: 20px;
  }
  .favorite_main{
    width: 1500px;
    padding: 0 120px 0 120px;
    position: relative;
    z-index: 100;
  }

  .draft_card{
    height: 79vh;
    width: 100%;
    background-color: #fff;
    overflow: scroll;
    -ms-overflow-style: none;
    scrollbar-width: none; /* Firefox */
  }
  .draft_card::-webkit-scrollbar {
    display: none; /* Chrome Safari */
  }

  .draft_card::-webkit-scrollbar {
    display: none; /* Chrome Safari */
  }

  .draftEmpty{
    width: 100%;
    height: 79vh;
    background-size: cover;
    -webkit-background-size: cover;
    -o-background-size: cover;
    background: url("../../assets/img/nodata.png") no-repeat center 0;
  }
</style>
