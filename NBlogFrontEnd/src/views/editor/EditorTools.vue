<template>
  <div>
    <el-row type="flex" class="row-bg" justify="space-between">
      <el-col :span="6">
        <el-input
          placeholder="请输入标题"
          prefix-icon="el-icon-edit"
          v-model="title">

        </el-input>
      </el-col>
      <el-col :span="12">
        <el-tag
          :key="tag"
          v-for="tag in dynamicTags"
          closable
          :disable-transitions="false"
          @close="handleClose(tag)">
          {{tag}}
        </el-tag>
        <el-input
          class="input-new-tag"
          v-if="inputVisible"
          v-model="inputValue"
          ref="saveTagInput"
          size="small"
          @keyup.enter.native="handleInputConfirm"
          @blur="handleInputConfirm"
        >
        </el-input>
        <el-button v-else class="button-new-tag" size="small" @click="showInput">+ New Tag</el-button>

      </el-col>
      <el-col :span="3">
        <div class="grid-content">
          <el-menu class="el-menu-demo" mode="horizontal">
            <el-submenu index="1">
              <template slot="title">
                {{scope}}
              </template>
              <el-menu-item index="1-1" @click="alertScope('公开')">公开</el-menu-item>
              <el-menu-item index="1-2" @click="alertScope('仅粉丝可见')">仅粉丝可见</el-menu-item>
              <el-menu-item index="1-3" @click="alertScope('私密')">私密</el-menu-item>
            </el-submenu>
          </el-menu>
        </div>
      </el-col>
    </el-row>

    <div id="editor">
      <mavon-editor
        ref="md"
        placeholder="请输入文档内容..."
        :boxShadow="false"
        style="border: 1px solid #d9d9d9;height:70vh;padding: 0;max-height: 580px"
        v-model="content"
        @imgAdd="$imgAdd"
        @imgDel="$imgDel"
        @save="saveMavon"
        @fullScreen="fullScreen"
        :subfield = "subfield"
        :ishljs="true"
        :codeStyle="code_style"
        :externalLink="externalLink"
      />
    </div>

    <br>
    <div>
      <el-row type="flex" class="row-bg font" justify="space-between">
        <el-col :span="7">
          <el-button @click="saveMavonToDraft">保存到草稿</el-button>
          <el-button @click="submitMavon" type="success">发表文章</el-button>

        </el-col>
        <el-col :span="12" class="latexTips_input">
          <el-col :span="6">latex 数学符号提示</el-col>
          <el-col :span="1" class="xie">\</el-col>
          <el-autocomplete
            class="inline-input"
            v-model="state1"
            :fetch-suggestions="querySearch"
            placeholder="请输入内容"
            @select="handleSelect"
          ></el-autocomplete>
        </el-col>

        <el-col :span="5">
          &nbsp;
        </el-col>
      </el-row>
    </div>
  </div>

</template>

<script>
  import {addArticle,uploadimg,getBase64ByUrl,delImg,
    getArticleInfo,addDraft,getDraftInfo} from "network/editor";

  let compressionImg=''

  export default {
    name: "Editor",
    data() {
      return {
        value: '',
        dynamicTags: ['随笔'],
        inputVisible: false,
        inputValue: '',
        scope:"公开",
        fullS:false,
        title:'',
        content:'',
        latexTips:[],
        state1: '',
        subfield: true,
        code_style: 'idea',
        externalLink: {
          markdown_css: function() {
            // 这是你的markdown css文件路径
            return './markdown/github-markdown.min.css';
          },
          hljs_js: function() {
            // 这是你的hljs文件路径
            return './highlightjs/highlight.min.js';
          },
          hljs_css: function(css) {
            // 这是你的代码高亮配色文件路径
            return './highlightjs/styles/' + css + '.min.css';
          },
          hljs_lang: function(lang) {
            // 这是你的代码高亮语言解析路径
            return './highlightjs/languages/' + lang + '.min.js';
          },
          katex_css: function() {
            // 这是你的katex配色方案路径路径
            return './katex/katex.min.css';
          },
          katex_js: function() {
            // 这是你的katex.js路径
            return './katex/katex.min.js';
          },
        }
      };
    },
    props:{

    },
    created() {

      if(localStorage.getItem("MDCont")!==null){
        this.$store.state.MDTitle=localStorage.getItem("MDTitle")
        this.$store.state.MDCont=localStorage.getItem("MDCont")
        this.$store.state.MDScope=localStorage.getItem("MDScope")
      }

      this.title=this.$store.state.MDTitle
      this.content=this.$store.state.MDCont
      this.scope=this.$store.state.MDScope

      if(this.$route.params.class==='reEditor'){
        getArticleInfo(this.$store.state.reEditor)
          .then(res => {
            this.title=res.data.extend.article.atitle
            this.scope=res.data.extend.article.ascope
            this.content=res.data.extend.article.acontent
            this.dynamicTags=res.data.extend.article.atags.split(",")

            this.$store.commit('save',this.content,this.title,this.scope)
            localStorage.setItem("MDCont",this.content)
            localStorage.setItem("MDTitle",this.title)
            localStorage.setItem("MDScope",this.scope)

            this.$message({
              message: '已为您自动保存！',
              type: 'success'
            });

          })
          .catch()
      }else if(this.$route.params.class==='reDraft'){
        getDraftInfo(this.$store.state.reEditor)
          .then(res => {
            this.title=res.data.extend.draft.atitle
            this.scope=res.data.extend.draft.ascope
            this.content=res.data.extend.draft.acontent
            this.dynamicTags=res.data.extend.draft.atags.split(",")

            this.$store.commit('save',this.content,this.title,this.scope)
            localStorage.setItem("MDCont",this.content)
            localStorage.setItem("MDTitle",this.title)
            localStorage.setItem("MDScope",this.scope)

            this.$message({
              message: '已为您自动保存！',
              type: 'success'
            });

          })
          .catch()
      }
    },
    methods: {
      saveMavonToDraft(){
        if(this.title.trim()==='' || this.title.length>30){
          this.$message("标题不能为空且长度不能超过30")
        }else{
          addDraft(this.title,this.dynamicTags,this.scope,this.content,this.$store.state.user)
            .then(res => {
              if(res.data.code === 100){
                this.title=''
                this.content=''
                this.dynamicTags=['随笔']
                this.scope='公开'
                this.$store.commit('clearMD')

                if(localStorage.getItem("MDTitle")!==null){
                  localStorage.removeItem("MDTitle")
                  localStorage.removeItem("MDCont")
                  localStorage.removeItem("MDScope")
                }

                this.$message({
                  message: '已加入草稿！',
                  type: 'success'
                });
              }else {
                this.$message({
                  message: '出错了，错误信息：'+res.data.extend.error,
                  type: 'error'
                });
              }
            }).catch(error => this.$message(error))
        }
      },
      submitMavon(){
        if(this.title.trim()==='' || this.title.length>30){
          this.$message("标题不能为空且长度不能超过30")
        }else if(this.content.trim()===''){
          this.$message("请输入内容")
        }else {
          addArticle(this.title,this.dynamicTags,this.scope,this.content,this.$store.state.user)
            .then(res => {
              if(res.data.code === 100){
                this.title=''
                this.content=''
                this.dynamicTags=['随笔']
                this.scope='公开'
                this.$store.commit('clearMD')

                if(localStorage.getItem("MDTitle")!==null){
                  localStorage.removeItem("MDTitle")
                  localStorage.removeItem("MDCont")
                  localStorage.removeItem("MDScope")
                }

                this.$message({
                  message: '发布成功！',
                  type: 'success'
                });
              }else {
                this.$message({
                  message: '出错了，错误信息：'+res.data.extend.error,
                  type: 'error'
                });
              }
            }).catch(error => this.$message(error))
        }
      },
      saveMavon(value,render){
        this.$store.commit('save',this.content,this.title,this.scope)
        localStorage.setItem("MDCont",this.content)
        localStorage.setItem("MDTitle",this.title)
        localStorage.setItem("MDScope",this.scope)

        this.$message({
          message: '保存成功！',
          type: 'success'
        });
      },
      fullScreen(){
        if(!this.fullS){
          this.$message({
            message: '开启全屏编辑模式',
            type: 'success'
          });
          document.getElementById("editor").children[0].style.minHeight="1000px";
          this.fullS=true
        }else{
          this.$message('退出全屏编辑模式');
          document.getElementById("editor").children[0].style.minHeight="300px";
          this.fullS=false
        }
      },
      $imgAdd(pos, $file) {
        let timeout=100
        if($file.size/100<50){
          timeout=100
        }else if($file.size/100<=200){
          timeout=200
        }else if($file.size/100<=1000){
          timeout=300
        }else if($file.size/100<3000){
          timeout=500
        }else {
          timeout=1000
        }

        this.base64ThumbImage($file,840,840)
        setTimeout(()=>{
          uploadimg(compressionImg)
          .then(res => {
            this.$refs.md.$img2Url(pos, res.data.extend.url);
            this.alertUrl()
            this.$message({
              message: '上传成功！',
              type: 'success'
            });
          })
            .catch(err => console.log(err))
        },timeout)
      },
      $imgDel(pos) {
        delImg(pos[0])
          .then((res) => {
            this.$message({
              message: '删除成功！',
              type: 'success'
            });
          })
          .catch(res => {
            console.log(res)
          })
      },
      handleClose(tag) {
        this.dynamicTags.splice(this.dynamicTags.indexOf(tag), 1);
      },
      showInput() {
        this.inputVisible = true;
        this.$nextTick(_ => {
          this.$refs.saveTagInput.$refs.input.focus();
        });
      },
      handleInputConfirm() {
        let inputValue = this.inputValue;
        if (inputValue) {
          if(this.dynamicTags.length>4){
            this.$message('最多5个标签哦~');
          }else
            this.dynamicTags.push(inputValue);
        }
        this.inputVisible = false;
        this.inputValue = '';
      },
      alertUrl(){
        this.$nextTick(() => {
          const imgs=document.getElementsByTagName("img")
          let urls=[]
          let count=0

          for(let i=2;i<imgs.length;i++){

            if(urls.indexOf(document.getElementsByTagName("img")[i].src.slice(document.getElementsByTagName("img")[i].src.lastIndexOf("/")))!==0){
              urls[count]=document.getElementsByTagName("img")[i].src.slice(document.getElementsByTagName("img")[i].src.lastIndexOf("/")).slice(1)
              count+=1
            }
          }
          getBase64ByUrl(urls,"now")
            .then(res=>{
              for(let i=0;i<res.data.extend.names.length;i++){
                this.$refs.md.$imgUpdateByUrl(res.data.extend.names[i], res.data.extend.url[i]);
              }
            })
            .catch(err => console.log(err))
        })
      },
      base64ThumbImage(elementObj,maxWidth,maxHeight){

        let current=""

        if (typeof (FileReader) === 'undefined') {
          console.log("抱歉，你的浏览器不支持发送图片，请升级浏览器或切换浏览器再试！");
        } else {
          try {
            const file = elementObj;
            // 压缩图片需要的一些元素和对象
            const reader = new FileReader(),
            //创建一个img对象
            img = new Image();
            reader.readAsDataURL(file);
            // 文件base64化，以便获知图片原始尺寸
            reader.onload = function (e) {
              img.src = e.target.result;
            };
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
              compressionImg=canvas.toDataURL('image/jpeg', 0.92)
            }
          } catch (e) {

          }
        }
      },
      alertScope(sc){
        this.scope=sc
      },
      querySearch(queryString, cb) {
        const latexTips = this.latexTips;
        const results = queryString ? latexTips.filter(this.createFilter(queryString)) : latexTips;
        // 调用 callback 返回建议列表的数据
        cb(results);
      },
      createFilter(queryString) {
        return (latexTips) => {
          return (latexTips.value.toLowerCase().indexOf(queryString.toLowerCase()) !== -1);
        };
      },
      handleSelect(item) {
        if(item['value']==='TextColor'){
          this.getValue(item['item'])
        }else {
          this.getValue("\\"+item['item'])
        }

        this.state1=''
      },
      loadLatexTips(){
        return [
          {"value":'acute',"item":'acute{a}'},
          {"value":'aleph',"item":'aleph'},
          {"value":'alpha',"item":'alpha'},
          {"value":'amalg',"item":'amalg'},
          {"value":'angle',"item":'angle'},
          {"value":'approx',"item":'approx'},
          {"value":'approxeq',"item":'approxeq'},
          {"value":'ast',"item":'ast'},
          {"value":'asymp',"item":'asymp'},
          {"value":'And',"item":'&'},

          {"value":'bar',"item":'bar{a}'},
          {"value":'barwedge',"item":'barwedge'},
          {"value":'bcancel',"item":'bcancel{a}'},
          {"value":'because',"item":'because'},
          {"value":'beta',"item":'beta'},
          {"value":'beth',"item":'beth'},
          {"value":'between',"item":'between'},
          {"value":'bigcap',"item":'bigcap'},
          {"value":'bigcirc',"item":'bigcirc'},
          {"value":'bigcup',"item":'bigcup'},
          {"value":'bigodot',"item":'bigodot'},
          {"value":'bigoplus',"item":'bigoplus'},
          {"value":'bigotimes',"item":'bigotimes'},
          {"value":'bigsqcup',"item":'bigsqcup'},
          {"value":'bigstar',"item":'bigstar'},
          {"value":'bigtriangledown',"item":'bigtriangledown'},
          {"value":'bigtriangleup',"item":'bigtriangleup'},
          {"value":'biguplus',"item":'biguplus'},
          {"value":'bigvee',"item":'bigvee'},
          {"value":'bigwedge',"item":'bigwedge'},
          {"value":'binom',"item":'binom{a}{b}'},
          {"value":'blacklozenge',"item":'blacklozenge'},
          {"value":'blacksquare',"item":'blacksquare'},
          {"value":'blacktriangle',"item":'blacktriangle'},
          {"value":'blacktriangledown',"item":'blacktriangledown'},
          {"value":'blacktriangleleft',"item":'blacktriangleleft'},
          {"value":'blacktriangleright',"item":'blacktriangleright'},
          {"value":'bm',"item":'bm{a}'},
          {"value":'bold',"item":'bold{a}'},
          {"value":'bot',"item":'bot'},
          {"value":'bowtie',"item":'bowtie'},
          {"value":'boxdot',"item":'boxdot'},
          {"value":'boxed',"item":'boxed{a}'},
          {"value":'boxminus',"item":'boxminus'},
          {"value":'boxplus',"item":'boxplus'},
          {"value":'boxtimes',"item":'boxtimes'},
          {"value":'brace',"item":'brace{a}{b}'},
          {"value":'brack',"item":'brack{a}{b}'},
          {"value":'breve',"item":'breve{a}'},
          {"value":'bullet',"item":'bullet'},
          {"value":'bumpeq',"item":'bumpeq'},

          {"value":'cancel',"item":'cancel{a}'},
          {"value":'cap',"item":'cap'},
          {"value":'cdot',"item":'cdot'},
          {"value":'cdotp',"item":'cdotp'},
          {"value":'cdots',"item":'cdots'},
          {"value":'centerdot',"item":'centerdot'},
          {"value":'cfrac',"item":'cfrac{a}{b}'},
          {"value":'check',"item":'check{a}'},
          {"value":'checkmark',"item":'checkmark'},
          {"value":'chi',"item":'chi'},
          {"value":'choose',"item":'choose'},
          {"value":'circ',"item":'circ'},
          {"value":'circeq',"item":'circeq'},
          {"value":'circlearrowleft',"item":'circlearrowleft'},
          {"value":'circlearrowright',"item":'circlearrowright'},
          {"value":'circledast',"item":'circledast'},
          {"value":'circledcirc',"item":'circledcirc'},
          {"value":'circleddash',"item":'circleddash'},
          {"value":'circledR',"item":'circledR'},
          {"value":'circledS',"item":'circledS'},
          {"value":'clubs',"item":'clubs'},
          {"value":'cnums',"item":'cnums'},
          {"value":'colon',"item":'colon'},
          {"value":'colonapprox',"item":'colonapprox'},
          {"value":'coloneq',"item":'coloneq'},
          {"value":'coloneqq',"item":'coloneqq'},
          {"value":'colonsim',"item":'colonsim'},
          {"value":'color',"item":'color{red}{b}'},
          {"value":'colorbox',"item":'colorbox{red}{b}'},
          {"value":'complement',"item":'complement'},
          {"value":'cong',"item":'cong'},
          {"value":'coprod',"item":'coprod'},
          {"value":'copyright',"item":'copyright'},
          {"value":'cup',"item":'cup'},
          {"value":'curlyeqprec',"item":'curlyeqprec'},
          {"value":'curlyeqsucc',"item":'curlyeqsucc'},
          {"value":'curlyvee',"item":'curlyvee'},
          {"value":'curlywedge',"item":'curlywedge'},
          {"value":'curvearrowleft',"item":'curvearrowleft'},
          {"value":'curvearrowright',"item":'curvearrowright'},
          {"value":'Cap',"item":'Cap'},
          {"value":'Chi',"item":'Chi'},
          {"value":'Cup',"item":'Cup'},

          {"value":'dag',"item":'dag'},
          {"value":'daleth',"item":'daleth'},
          {"value":'darr',"item":'darr'},
          {"value":'dashleftarrow',"item":'dashleftarrow'},
          {"value":'dashrightarrow',"item":'dashrightarrow'},
          {"value":'dashv',"item":'dashv'},
          {"value":'dbinom',"item":'dbinom{a}{b}'},
          {"value":'dblcolon',"item":'dblcolon'},
          {"value":'ddag',"item":'ddag'},
          {"value":'ddot',"item":'ddot{a}'},
          {"value":'ddots',"item":'ddots'},
          {"value":'degree',"item":'degree'},
          {"value":'delta',"item":'delta'},
          {"value":'dfrac',"item":'dfrac{a}{b}'},
          {"value":'diagdown',"item":'diagdown'},
          {"value":'diagup',"item":'diagup'},
          {"value":'diamond',"item":'diamond'},
          {"value":'diamonds',"item":'diamonds'},
          {"value":'digamma',"item":'digamma'},
          {"value":'div',"item":'div'},
          {"value":'divideontimes',"item":'divideontimes'},
          {"value":'dot',"item":'dot{a}'},
          {"value":'doteq',"item":'doteq'},
          {"value":'doteqdot',"item":'doteqdot'},
          {"value":'dotplus',"item":'dotplus'},
          {"value":'dotsb',"item":'dotsb'},
          {"value":'doublebarwedge',"item":'doublebarwedge'},
          {"value":'downarrow',"item":'downarrow'},
          {"value":'downharpoonright',"item":'downharpoonright'},
          {"value":'downharpoonleft',"item":'downharpoonleft'},
          {"value":'dArr',"item":'dArr'},
          {"value":'Delta',"item":'Delta'},

          {"value":'ell',"item":'ell'},
          {"value":'empty',"item":'empty'},
          {"value":'emptyset',"item":'emptyset'},
          {"value":'epsilon',"item":'epsilon'},
          {"value":'eqcirc',"item":'eqcirc'},
          {"value":'eqsim',"item":'eqsim'},
          {"value":'eqslantgtr',"item":'eqslantgtr'},
          {"value":'eqslantless',"item":'eqslantless'},
          {"value":'equiv',"item":'equiv'},
          {"value":'eta',"item":'eta'},
          {"value":'eth',"item":'eth'},
          {"value":'exist',"item":'exist'},
          {"value":'exists',"item":'exists'},
          {"value":'exp',"item":'exp'},
          {"value":'Epsilon',"item":'Epsilon'},
          {"value":'Eqcolon',"item":'Eqcolon'},
          {"value":'Eta',"item":'Eta'},

          {"value":'fallingdotseq',"item":'fallingdotseq'},
          {"value":'flat',"item":'flat'},
          {"value":'forall',"item":'forall'},
          {"value":'frac',"item":'frac{a}{b}'},
          {"value":'frak',"item":'frak{a}'},
          {"value":'frown',"item":'frown'},
          {"value":'Finv',"item":'Finv'},

          {"value":'gamma',"item":'gamma'},
          {"value":'ge',"item":'ge'},
          {"value":'geq',"item":'geq'},
          {"value":'geqq',"item":'geqq'},
          {"value":'geqslant',"item":'geqslant'},
          {"value":'gets',"item":'gets'},
          {"value":'gg',"item":'gg'},
          {"value":'ggg',"item":'ggg'},
          {"value":'gimel',"item":'gimel'},
          {"value":'gnapprox',"item":'gnapprox'},
          {"value":'gneq',"item":'gneq'},
          {"value":'gneqq',"item":'gneqq'},
          {"value":'gnsim',"item":'gnsim'},
          {"value":'grave',"item":'grave{a}'},
          {"value":'gt',"item":'gt'},
          {"value":'gtrapprox',"item":'gtrapprox'},
          {"value":'gtrdot',"item":'gtrdot'},
          {"value":'gtreqless',"item":'gtreqless'},
          {"value":'gtreqqless',"item":'gtreqqless'},
          {"value":'gtrless',"item":'gtrless'},
          {"value":'gvertneqq',"item":'gvertneqq'},
          {"value":'Game',"item":'Game'},
          {"value":'Gamma',"item":'Gamma'},

          {"value":'harr',"item":'harr'},
          {"value":'hat',"item":'hat{a}'},
          {"value":'hbar',"item":'hbar'},
          {"value":'hearts',"item":'hearts'},
          {"value":'hookleftarrow',"item":'hookleftarrow'},
          {"value":'hookrightarrow',"item":'hookrightarrow'},
          {"value":'hArr',"item":'hArr'},
          {"value":'Huge',"item":'Huge'},
          {"value":'huge',"item":'huge'},

          {"value":'iff',"item":'iff'},
          {"value":'iiint',"item":'iiint'},
          {"value":'iint',"item":'iint'},
          {"value":'image',"item":'image'},
          {"value":'imath',"item":'imath'},
          {"value":'impliedby',"item":'impliedby'},
          {"value":'implies',"item":'implies'},
          {"value":'in',"item":'in'},
          {"value":'infin',"item":'infin'},
          {"value":'infty',"item":'infty'},
          {"value":'int',"item":'int'},
          {"value":'intercal',"item":'intercal'},
          {"value":'intop',"item":'intop'},
          {"value":'iota',"item":'iota'},
          {"value":'isin',"item":'isin'},
          {"value":'Iota',"item":'Iota'},

          {"value":'jmath',"item":'jmath'},
          {"value":'Join',"item":'Join'},

          {"value":'kappa',"item":'kappa'},
          {"value":'Kappa',"item":'Kappa'},

          {"value":'lambda',"item":'lambda'},
          {"value":'land',"item":'land'},
          {"value":'lang',"item":'lang'},
          {"value":'langle',"item":'langle'},
          {"value":'larr',"item":'larr'},
          {"value":'lbrack',"item":'lbrack'},
          {"value":'lceil',"item":'lceil'},
          {"value":'ldotp',"item":'ldotp'},
          {"value":'ldots',"item":'ldots'},
          {"value":'le',"item":'le'},
          {"value":'leadsto',"item":'leadsto'},
          {"value":'leftarrow',"item":'leftarrow'},
          {"value":'leftarrowtail',"item":'leftarrowtail'},
          {"value":'leftharpoondown',"item":'leftharpoondown'},
          {"value":'leftleftarrows',"item":'leftleftarrows'},
          {"value":'leftrightarrow',"item":'leftrightarrow'},
          {"value":'leftrightarrows',"item":'leftrightarrows'},
          {"value":'leftrightharpoons',"item":'leftrightharpoons'},
          {"value":'leftrightsquigarrow',"item":'leftrightsquigarrow'},
          {"value":'leftthreetimes',"item":'leftthreetimes'},
          {"value":'leq',"item":'leq'},
          {"value":'leqq',"item":'leqq'},
          {"value":'leqslant',"item":'leqslant'},
          {"value":'lessapprox',"item":'lessapprox'},
          {"value":'lessdot',"item":'lessdot'},
          {"value":'lesseqgtr',"item":'lesseqgtr'},
          {"value":'lesseqqgtr',"item":'lesseqqgtr'},
          {"value":'lessgtr',"item":'lessgtr'},
          {"value":'lesssim',"item":'lesssim'},
          {"value":'lfloor',"item":'lfloor'},
          {"value":'lgroup',"item":'lgroup'},
          {"value":'lhd',"item":'lhd'},
          {"value":'ll',"item":'ll'},
          {"value":'lll',"item":'lll'},
          {"value":'lnapprox',"item":'lnapprox'},
          {"value":'lneq',"item":'lneq'},
          {"value":'lneqq',"item":'lneqq'},
          {"value":'longleftarrow',"item":'longleftarrow'},
          {"value":'longleftrightarrow',"item":'longleftrightarrow'},
          {"value":'longmapsto',"item":'longmapsto'},
          {"value":'longrightarrow',"item":'longrightarrow'},
          {"value":'looparrowleft',"item":'looparrowleft'},
          {"value":'looparrowright',"item":'looparrowright'},
          {"value":'lor',"item":'lor'},
          {"value":'lozenge',"item":'lozenge'},
          {"value":'lparen',"item":'lparen'},
          {"value":'lq',"item":'lq'},
          {"value":'lrarr',"item":'lrarr'},
          {"value":'Lambda',"item":'Lambda'},

          {"value":'nabla',"item":'nabla'},
          {"value":'natnums',"item":'natnums'},
          {"value":'natural',"item":'natural'},
          {"value":'ncong',"item":'ncong'},
          {"value":'ne',"item":'ne'},
          {"value":'nearrow',"item":'nearrow'},
          {"value":'neq',"item":'neq'},
          {"value":'nexists',"item":'nexists'},
          {"value":'ngeq',"item":'ngeq'},
          {"value":'ngeqq',"item":'ngeqq'},
          {"value":'ngeqslant',"item":'ngeqslant'},
          {"value":'ngtr',"item":'ngtr'},
          {"value":'ni',"item":'ni'},
          {"value":'nleftarrow',"item":'nleftarrow'},
          {"value":'nleftrightarrow',"item":'nleftrightarrow'},
          {"value":'nleq',"item":'nleq'},
          {"value":'nleqq',"item":'nleqq'},
          {"value":'nleqslant',"item":'nleqslant'},
          {"value":'nless',"item":'nless'},
          {"value":'nmid',"item":'nmid'},
          {"value":'not =',"item":'not ={a}'},
          {"value":'notin',"item":'notin'},
          {"value":'notni',"item":'notni'},
          {"value":'nparallel',"item":'nparallel'},
          {"value":'nrightarrow',"item":'nrightarrow'},
          {"value":'nsim',"item":'nsim'},
          {"value":'nsubseteq',"item":'nsubseteq'},
          {"value":'nsubseteqq',"item":'nsubseteqq'},
          {"value":'nsucc',"item":'nsucc'},
          {"value":'nsupseteq',"item":'nsupseteq'},
          {"value":'nsupseteqq',"item":'nsupseteqq'},
          {"value":'ntriangleleft',"item":'ntriangleleft'},
          {"value":'nu',"item":'nu'},
          {"value":'nvdash',"item":'nvdash'},
          {"value":'N',"item":'N'},
          {"value":'Nu',"item":'Nu'},

          {"value":'maltese',"item":'maltese'},
          {"value":'mapsto',"item":'mapsto'},
          {"value":'mathbf',"item":'mathbf{a}'},
          {"value":'mathbin',"item":'mathbin{a}'},
          {"value":'mathcal',"item":'mathcal{A}'},
          {"value":'mathclap',"item":'mathclap{A}'},
          {"value":'mathellipsis',"item":'mathellipsis'},
          {"value":'mathfrak',"item":'mathfrak{a}'},
          {"value":'mathinner',"item":'mathinner{A}'},
          {"value":'mathring',"item":'mathring{a}'},
          {"value":'mathrm',"item":'mathrm{a}'},
          {"value":'max',"item":'max'},
          {"value":'min',"item":'min'},
          {"value":'mho',"item":'mho'},
          {"value":'mid',"item":'mid'},
          {"value":'mp',"item":'mp'},
          {"value":'mu',"item":'mu'},
          {"value":'multimap',"item":'multimap'},
          {"value":'Mu',"item":'Mu'},

          {"value":'odot',"item":'odot'},
          {"value":'oiiint',"item":'oiiint'},
          {"value":'oiint',"item":'oiint'},
          {"value":'oint',"item":'oint'},
          {"value":'omega',"item":'omega'},
          {"value":'omicron',"item":'omicron'},
          {"value":'ominus',"item":'ominus'},
          {"value":'oplus',"item":'oplus'},
          {"value":'oslash',"item":'oslash'},
          {"value":'otimes',"item":'otimes'},
          {"value":'over',"item":'over a'},
          {"value":'overbrace',"item":'overbrace{a}'},
          {"value":'overgroup',"item":'overgroup{a}'},
          {"value":'overleftarrow',"item":'overleftarrow{a}'},
          {"value":'overleftharpoon',"item":'overleftharpoon{a}'},
          {"value":'overleftrightarrow',"item":'overleftrightarrow{a}'},
          {"value":'overline',"item":'overline{a}'},
          {"value":'overlinesegment',"item":'overlinesegment{a}'},
          {"value":'overrightarrow',"item":'overrightarrow{a}'},
          {"value":'overrightharpoon',"item":'overrightharpoon{a}'},
          {"value":'overset',"item":'overset{a}{b}'},
          {"value":'owns',"item":'owns'},
          {"value":'Omega',"item":'Omega'},
          {"value":'Omicron',"item":'Omicron'},
          {"value":'Overrightarrow',"item":'Overrightarrow{a}'},

          {"value":'parallel',"item":'parallel'},
          {"value":'partial',"item":'partial'},
          {"value":'perp',"item":'perp'},
          {"value":'phi',"item":'phi'},
          {"value":'pi',"item":'pi'},
          {"value":'pitchfork',"item":'pitchfork'},
          {"value":'pm',"item":'pm'},
          {"value":'plusmn',"item":'plusmn'},
          {"value":'pmod',"item":'pmod a'},
          {"value":'pod',"item":'pod a'},
          {"value":'pounds',"item":'pounds'},
          {"value":'prec',"item":'prec'},
          {"value":'precapprox',"item":'precapprox'},
          {"value":'preccurlyeq',"item":'preccurlyeq'},
          {"value":'preceq',"item":'preceq'},
          {"value":'precnapprox',"item":'precnapprox'},
          {"value":'precneqq',"item":'precneqq'},
          {"value":'precnsim',"item":'precnsim'},
          {"value":'precsim',"item":'precsim'},
          {"value":'prime',"item":'prime'},
          {"value":'prod',"item":'prod'},
          {"value":'propto',"item":'propto'},
          {"value":'psi',"item":'psi'},
          {"value":'Pi',"item":'Pi'},
          {"value":'Pr',"item":'Pr'},
          {"value":'Psi',"item":'Psi'},

          {"value":'qquad',"item":'qquad'},
          {"value":'quad',"item":'quad'},

          {"value":'rang',"item":'rang'},
          {"value":'rangle',"item":'rangle'},
          {"value":'rarr',"item":'rarr'},
          {"value":'rbrack',"item":'rbrack'},
          {"value":'rceil',"item":'rceil'},
          {"value":'real',"item":'real'},
          {"value":'reals',"item":'reals'},
          {"value":'restriction',"item":'restriction'},
          {"value":'rfloor',"item":'rfloor'},
          {"value":'rgroup',"item":'rgroup'},
          {"value":'rhd',"item":'rhd'},
          {"value":'rho',"item":'rho'},
          {"value":'rightarrow',"item":'rightarrow'},
          {"value":'rightarrowtail',"item":'rightarrowtail'},
          {"value":'rightharpoondown',"item":'rightharpoondown'},
          {"value":'rightharpoonup',"item":'rightharpoonup'},
          {"value":'rightleftarrows',"item":'rightleftarrows'},
          {"value":'rightleftharpoons',"item":'rightleftharpoons'},
          {"value":'rightrightarrows',"item":'rightrightarrows'},
          {"value":'rightsquigarrow',"item":'rightsquigarrow'},
          {"value":'rightthreetimes',"item":'rightthreetimes'},
          {"value":'risingdotseq',"item":'risingdotseq'},
          {"value":'rlap',"item":'rlap{a}'},
          {"value":'rparen',"item":'rparen'},
          {"value":'rq',"item":'rq'},
          {"value":'rtimes',"item":'rtimes'},
          {"value":'rvert',"item":'rvert a'},
          {"value":'rArr',"item":'rArr'},
          {"value":'rVert',"item":'rVert'},
          {"value":'R',"item":'R'},
          {"value":'Rarr',"item":'Rarr'},
          {"value":'Re',"item":'Re'},
          {"value":'Reals',"item":'Reals'},
          {"value":'Rsh',"item":'Rsh'},

          {"value":'scriptscriptstyle',"item":'scriptscriptstyle a'},
          {"value":'scriptsize',"item":'scriptsize a'},
          {"value":'scriptstyle',"item":'scriptstyle a'},
          {"value":'sdot',"item":'sdot'},
          {"value":'searrow',"item":'searrow'},
          {"value":'sec',"item":'sec'},
          {"value":'setminus',"item":'setminus'},
          {"value":'sf',"item":'sf a'},
          {"value":'sh',"item":'sh'},
          {"value":'sharp',"item":'sharp'},
          {"value":'sigma',"item":'sigma'},
          {"value":'sim',"item":'sim'},
          {"value":'simeq',"item":'simeq'},
          {"value":'sin',"item":'sin'},
          {"value":'sinh',"item":'sinh'},
          {"value":'small',"item":'small a'},
          {"value":'smallfrown',"item":'smallfrown'},
          {"value":'smile',"item":'smile'},
          {"value":'sout',"item":'sout{a}'},
          {"value":'spades',"item":'spades'},
          {"value":'sphericalangle',"item":'sphericalangle'},
          {"value":'sqcap',"item":'sqcap'},
          {"value":'sqcup',"item":'sqcup'},
          {"value":'sqrt',"item":'sqrt{a}'},
          {"value":'sqsubset',"item":'sqsubset'},
          {"value":'square',"item":'square'},
          {"value":'stackrel',"item":'stackrel{a}{b}'},
          {"value":'star',"item":'star'},
          {"value":'sube',"item":'sube'},
          {"value":'subset',"item":'subset'},
          {"value":'subseteq',"item":'subseteq'},
          {"value":'subseteqq',"item":'subseteqq'},
          {"value":'subsetneq',"item":'subsetneq'},
          {"value":'subsetneqq',"item":'subsetneqq'},
          {"value":'sum',"item":'sum'},
          {"value":'sup',"item":'sup'},
          {"value":'supset',"item":'supset'},
          {"value":'supseteq',"item":'supseteq'},
          {"value":'supseteqq',"item":'supseteqq'},
          {"value":'supsetneq',"item":'supsetneq'},
          {"value":'supsetneqq',"item":'supsetneqq'},
          {"value":'surd',"item":'surd'},
          {"value":'swarrow',"item":'swarrow'},
          {"value":'Sigma',"item":'Sigma'},
          {"value":'Subset',"item":'Subset'},
          {"value":'Supset',"item":'Supset'},

          {"value":'tbinom',"item":'tbinom'},
          {"value":'text',"item":'text{a}'},
          {"value":'textbf',"item":'textbf{a}'},
          {"value":'textcolor',"item":'textcolor{red}{a}'},
          {"value":'textit',"item":'textit{a}'},
          {"value":'textnormal',"item":'textnormal{a}'},
          {"value":'textrm',"item":'textrm{a}'},
          {"value":'textsf',"item":'textsf{a}'},
          {"value":'therefore',"item":'therefore'},
          {"value":'theta',"item":'theta'},
          {"value":'thetasym',"item":'thetasym'},
          {"value":'thickapprox',"item":'thickapprox'},
          {"value":'thicksim',"item":'thicksim'},
          {"value":'tilde',"item":'tilde{a}'},
          {"value":'times',"item":'times'},
          {"value":'tiny',"item":'tiny a'},
          {"value":'to',"item":'to'},
          {"value":'top',"item":'top'},
          {"value":'triangle',"item":'triangle'},
          {"value":'triangledown',"item":'triangledown'},
          {"value":'triangleleft',"item":'triangleleft'},
          {"value":'trianglelefteq',"item":'trianglelefteq'},
          {"value":'triangleq',"item":'triangleq'},
          {"value":'triangleright',"item":'triangleright'},
          {"value":'trianglerighteq',"item":'trianglerighteq'},
          {"value":'twoheadleftarrow',"item":'twoheadleftarrow'},
          {"value":'twoheadrightarrow',"item":'twoheadrightarrow'},
          {"value":'Tau',"item":'Tau'},
          {"value":'TeX',"item":'TeX'},
          {"value":'Theta',"item":'Theta'},

          {"value":'uarr',"item":'uarr'},
          {"value":'ulcorner',"item":'ulcorner'},
          {"value":'underleftarrow',"item":'underleftarrow{a}'},
          {"value":'underleftrightarrow',"item":'underleftrightarrow{a}'},
          {"value":'underline',"item":'underline{a}'},
          {"value":'underlinesegment',"item":'underlinesegment{a}'},
          {"value":'underrightarrow',"item":'underrightarrow{a}'},
          {"value":'underset',"item":'underset{a}{b}'},
          {"value":'unlhd',"item":'unlhd'},
          {"value":'unrhd',"item":'unrhd'},
          {"value":'uparrow',"item":'uparrow'},
          {"value":'updownarrow',"item":'updownarrow'},
          {"value":'upharpoonleft',"item":'upharpoonleft'},
          {"value":'upharpoonright',"item":'upharpoonright'},
          {"value":'uplus',"item":'uplus'},
          {"value":'upsilon',"item":'upsilon'},
          {"value":'upuparrows',"item":'upuparrows'},
          {"value":'utilde',"item":'utilde{a}'},
          {"value":'Upsilon',"item":'Upsilon'},

          {"value":'varepsilon',"item":'varepsilon'},
          {"value":'varkappa',"item":'varkappa'},
          {"value":'varnothing',"item":'varnothing'},
          {"value":'varphi',"item":'varphi'},
          {"value":'varpi',"item":'varpi'},
          {"value":'varpropto',"item":'varpropto'},
          {"value":'varrho',"item":'varrho'},
          {"value":'varsigma',"item":'varsigma'},
          {"value":'varsubsetneq',"item":'varsubsetneq'},
          {"value":'vartheta',"item":'vartheta'},
          {"value":'varDelta',"item":'varDelta'},
          {"value":'varGamma',"item":'varGamma'},
          {"value":'varLambda',"item":'varLambda'},
          {"value":'varOmega',"item":'varOmega'},
          {"value":'varPhi',"item":'varPhi'},
          {"value":'varPi',"item":'varPi'},
          {"value":'varPsi',"item":'varPsi'},
          {"value":'varSigma',"item":'varSigma'},
          {"value":'varTheta',"item":'varTheta'},
          {"value":'varUpsilon',"item":'varUpsilon'},
          {"value":'varXi',"item":'varXi'},
          {"value":'vcentcolon',"item":'vcentcolon'},
          {"value":'vdash',"item":'vdash'},
          {"value":'vdots',"item":'vdots'},
          {"value":'vec',"item":'vec{a}'},
          {"value":'veebar',"item":'veebar'},
          {"value":'vert',"item":'vert'},

          {"value":'wedge',"item":'wedge'},
          {"value":'weierp',"item":'weierp'},
          {"value":'widecheck',"item":'widecheck{a}'},
          {"value":'widehat',"item":'widehat{a}'},
          {"value":'widetilde',"item":'widetilde{a}'},
          {"value":'wp',"item":'wp'},
          {"value":'wr',"item":'wr'},

          {"value":'xcancel',"item":'xcancel'},
          {"value":'xhookleftarrow',"item":'xhookleftarrow{a}'},
          {"value":'xhookrightarrow',"item":'xhookrightarrow{a}'},
          {"value":'xi',"item":'xi'},
          {"value":'xleftarrow',"item":'xleftarrow{a}'},
          {"value":'xleftharpoondown',"item":'xleftharpoondown{a}'},
          {"value":'xleftharpoonup',"item":'xleftharpoonup{a}'},
          {"value":'xleftrightarrow',"item":'xleftrightarrow{a}'},
          {"value":'xleftrightharpoons',"item":'xleftrightharpoons{a}'},
          {"value":'xlongequal',"item":'xlongequal{a}'},
          {"value":'xmapsto',"item":'xmapsto{a}'},
          {"value":'xrightarrow',"item":'xrightarrow{a}'},
          {"value":'xrightharpoondown',"item":'xrightharpoondown{a}'},
          {"value":'xrightharpoonup',"item":'xrightharpoonup{a}'},
          {"value":'xrightleftharpoons',"item":'xrightleftharpoons{a}'},
          {"value":'xtofrom',"item":'xtofrom{a}'},
          {"value":'xtwoheadleftarrow',"item":'xtwoheadleftarrow{a}'},
          {"value":'xtwoheadrightarrow',"item":'xtwoheadrightarrow{a}'},
          {"value":'xLeftarrow',"item":'xLeftarrow{a}'},
          {"value":'xLeftrightarrow',"item":'xLeftrightarrow{a}'},
          {"value":'xRightarrow',"item":'xRightarrow{a}'},
          {"value":'Xi',"item":'Xi'},

          {"value":'yen',"item":'yen'},

          {"value":'zeta',"item":'zeta'},
          {"value":'Z',"item":'Z'},
          {"value":'Zeta',"item":'Zeta'},

          {"value":'TextColor',"item":"<font color='red'>a</font>"},
        ]
      },
      getValue(str){
        var myField=document.getElementsByTagName("textarea")[0];
        //IE浏览器
        if (document.selection) {
          myField.focus();
          sel = document.selection.createRange();
          sel.text = str;
          sel.select();
        }

        //火狐/网景 浏览器
        else if (myField.selectionStart || myField.selectionStart === '0')
        {
          //得到光标前的位置
          var startPos = myField.selectionStart;
          //得到光标后的位置
          var endPos = myField.selectionEnd;
          // 在加入数据之前获得滚动条的高度
          var restoreTop = myField.scrollTop;
          myField.value = myField.value.substring(0, startPos) + str + myField.value.substring(endPos, myField.value.length);
          //如果滚动条高度大于0
          if (restoreTop > 0) {
            // 返回
            myField.scrollTop = restoreTop;
          }
          myField.focus();
          myField.selectionStart = startPos + str.length;
          myField.selectionEnd = startPos + str.length;
        }
        else {
          myField.value += str;
          myField.focus();
        }

        this.content=myField.value
      }
    },
    mounted() {
      this.alertUrl()
      this.latexTips=this.loadLatexTips()
      // hljs.highlightCode()
    }
  }
</script>

<style scoped>
  .row-bg{
    align-items:center;
  }
  .el-tag + .el-tag {
    margin-left: 10px;
  }
  .button-new-tag {
    margin-left: 10px;
    height: 32px;
    line-height: 30px;
    padding-top: 0;
    padding-bottom: 0;
  }
  .input-new-tag {
    width: 90px;
    margin-left: 10px;
    vertical-align: bottom;
  }
  #latexTips{
    display: none;
    position: absolute;
    top: 0;
    left: 0;
    z-index: 1666;
  }
  .latexTips_input{
    display: flex;
    align-items:center;
    justify-content:start;

  }
  .xie{
    font-size: 22px;
  }


</style>
