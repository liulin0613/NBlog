<template>
  <div>
    <div style="padding:5px;">
      <Breadcrumb :route="breadcrumb"/>
      <el-container>
        <el-main>
          <div v-if="drafts.length===0">
            <h1>草稿箱为空</h1>
          </div>

          <div v-else id="block">
            <el-timeline>
              <el-timeline-item v-for="(item,i) in drafts" :timestamp="item.atime" placement="top">
                <el-card>
                  <h1>
                    <span>
                      {{item.atitle}}
                    </span>
                  </h1>
                  <br>
                  <div>
                    <el-button type="text">操作：</el-button>
                    &nbsp;
                    <el-button @click="editor(item.aid)" slot="reference"  type="primary" size="mini" icon="el-icon-edit" round>编辑</el-button>
                    &nbsp;&nbsp;
                    <el-popconfirm :title="'确定删除草稿 -- '+item.atitle" @confirm="deleteDraft(item.aid,i)">
                      <el-button slot="reference" type="danger" size="mini" icon="el-icon-delete" round> 删除</el-button>
                    </el-popconfirm>

                  </div>
                </el-card>
              </el-timeline-item>

            </el-timeline>
          </div>
        </el-main>
        <el-aside width="350px">
          &nbsp;
        </el-aside>
      </el-container>
    </div>
  </div>
</template>

<script>
  import Breadcrumb from "components/content/breadcrumb/Breadcrumb";
  import {getAllDraft,deleteDraft} from "network/draft";

  export default {
    name: "DraftMain",
    components:{
      Breadcrumb
    },
    data(){
      return {
        breadcrumb:[
          {title:'首页',path:'/nblog/new'},
          {title:'草稿'}
        ],
        drafts:[],
      }
    },
    created() {
      getAllDraft(this.$store.state.user)
        .then(res => {
          this.drafts=res.data.extend.drafts
        })
        .catch(err => console.log(err))
    },
    methods:{
      editor(did){
        deleteDraft(did)
          .then(res => {
            this.$store.state.reEditor=did
            this.$router.push('/nblog/publish/reDraft')
          })
          .catch(err => this.$message.error("删除失败"))
      },
      deleteDraft(did,i){
        deleteDraft(did)
          .then(res => {
            this.$message({
              type:"success",
              message:"删除--"+this.drafts[i].atitle+"--成功"
            })
            this.drafts.splice(i,1)
          })
          .catch(err => this.$message.error("删除失败"))
      },
    }
  }
</script>

<style scoped>

</style>
