<template>
  <div>
    <el-container>
      <el-header class="noPrint">
        <Header/>
      </el-header>
      <el-main style="z-index: 999;display:flex; justify-content: center">
        <div class="adjust_width  published_main">
          <el-container>
            <el-aside class="noPrint" style="width:150px">
              <Nav :activate_nav="'已发表'"/>
            </el-aside>
            <el-main>
              <div class="published_card">
                <el-row>
                  <el-col class="noPrint" :span="2">
                    <div style="height: 80vh;width: 100%; float: left">
                      <div class="pnav">
                        <div class="pnav_main">
                          <div class="pnav_icon">
                            <img src="@/assets/img/index.png" />
                          </div>
                          <br><br><br>

                          <div class="pnav_add" @click="publish()">
                            <el-tooltip class="item" effect="dark" content="发表文章" placement="right">
                              <svg t="1639294861459" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="20433" width="32" height="32"><path d="M67.8 692l40-249.1c2.3-14.3 9.6-27.2 20.7-36.5L504.9 90.1c50.7-42.6 126.4-36 168.9 14.8L809 266.1c42.6 50.8 36 126.5-14.8 169.1L417.7 751.6c-11.1 9.3-25.1 14.3-39.5 14.1l-251.9-3.9c-36.7-0.6-64.3-33.6-58.5-69.8z m125.3-212.7L167.6 638c-2.9 18 10.9 34.5 29.1 34.7l160.5 2.4c7.2 0.1 14.2-2.4 19.7-7l336.4-282.6c25.4-21.3 28.7-59.2 7.4-84.6l-96.4-115.1c-21.3-25.4-59.1-28.7-84.5-7.4L203.4 461.1c-5.5 4.6-9.2 11.1-10.3 18.2zM916.9 962H107.1C82.3 962 62 941.7 62 916.9s20.3-45.1 45.1-45.1H917c24.8 0 45.1 20.3 45.1 45.1-0.1 24.8-20.4 45.1-45.2 45.1zM916.9 781.8H647.1c-24.8 0-45.1-20.3-45.1-45.1s20.3-45.1 45.1-45.1H917c24.8 0 45.1 20.3 45.1 45.1-0.1 24.8-20.4 45.1-45.2 45.1z" fill="#8a8a8a" p-id="20434"></path></svg>
                            </el-tooltip>
                          </div>
                          <br>
                          <div class="pnav_search" @click="alertShow(0)">
                            <el-tooltip class="item" effect="dark" content="搜索" placement="right">
                              <svg t="1639294724121" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="18085" width="32" height="32"><path d="M955.069071 864.311021 740.015134 649.258107c-3.752464-3.751441-8.841366-5.860475-14.149255-5.860475-5.306866 0-10.395768 2.108011-14.149255 5.860475l-16.692171 16.692171-38.34226-38.34226c53.03796-59.810201 85.298711-138.442072 85.298711-224.478588 0-186.774871-151.952784-338.727655-338.727655-338.727655S64.527642 216.35456 64.527642 403.12943c0 186.775894 151.952784 338.728678 338.727655 338.728678 86.36909 0 165.276231-32.510438 225.170343-85.913718l38.303374 38.303374-17.34504 17.34504c-7.812943 7.813966-7.812943 20.48352 0 28.297486l215.051891 215.052914c3.753487 3.751441 8.841366 5.860475 14.149255 5.860475 5.306866 0 10.395768-2.108011 14.149255-5.860475l62.334697-62.334697C962.883037 884.794541 962.883037 872.124987 955.069071 864.311021zM104.546078 403.12943c0-164.709319 133.9999-298.709219 298.709219-298.709219s298.709219 133.9999 298.709219 298.709219S567.964616 701.839673 403.255297 701.839673 104.546078 567.838749 104.546078 403.12943zM878.585119 912.496463 691.829691 725.741036l34.036187-34.036187 186.755428 186.755428L878.585119 912.496463z" p-id="18086" fill="#8a8a8a"></path></svg>
                            </el-tooltip>
                          </div>

                          <br><br>

                          <div class="pnav_file" @click="alertShow(-1)">
                            <el-tooltip class="item" effect="dark" content="未归档文件" placement="right">
                              <svg t="1639294429117" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="12539" width="32" height="32"><path d="M179.2 640h61.2864a76.8 76.8 0 0 1 42.5984 12.9024L417.3312 742.4h189.3376l134.2464-89.4976a76.8 76.8 0 0 1 42.5984-12.9024H844.8V563.2a25.6 25.6 0 0 1 51.2 0v307.2a76.8 76.8 0 0 1-76.8 76.8H204.8A76.8 76.8 0 0 1 128 870.4V204.8A76.8 76.8 0 0 1 204.8 128h614.4A76.8 76.8 0 0 1 896 204.8v186.0096a25.6 25.6 0 0 1-51.2 0V204.8a25.6 25.6 0 0 0-25.6-25.6H204.8a25.6 25.6 0 0 0-25.6 25.6v435.2z m0 51.2V870.4a25.6 25.6 0 0 0 25.6 25.6h614.4a25.6 25.6 0 0 0 25.6-25.6v-179.2h-61.2864a25.6 25.6 0 0 0-14.2336 4.3008l-135.5264 90.368a46.08 46.08 0 0 1-25.6 7.7312H415.8464a46.08 46.08 0 0 1-25.6-7.7312l-135.4752-90.368a25.6 25.6 0 0 0-14.2336-4.3008H179.2z m153.6-384h358.4a25.6 25.6 0 0 1 0 51.2h-358.4a25.6 25.6 0 0 1 0-51.2z m0 153.6h358.4a25.6 25.6 0 0 1 0 51.2h-358.4a25.6 25.6 0 0 1 0-51.2z m102.4 153.6h153.6a25.6 25.6 0 0 1 0 51.2h-153.6a25.6 25.6 0 0 1 0-51.2z" fill="#9DA7B2" p-id="12540"></path></svg>
                            </el-tooltip>
                          </div>

                          <br>

                          <div class="pnav_dir" @click="alertShow(1)">
                            <el-tooltip class="item" effect="dark" content="文件夹" placement="right">
                              <svg t="1639294389620" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="11588" width="32" height="32"><path d="M846.027 806.867v-584.35H422.531L325.384 99.676H64.21v153.511h0.002v553.68h781.815z m-725.162-523.01h668.508v461.669H120.865V283.857z" fill="#839495" p-id="11589"></path><path d="M902.68 370.968v495.91H268.164v61.341h691.169V370.968z" fill="#839495" p-id="11590"></path></svg>
                            </el-tooltip>
                          </div>

                          <br><br><br>

                          <div class="pnav_ana" @click="openAnalysis()"  >
                            <el-tooltip class="item" effect="dark" content="分析" placement="right">
                              <svg t="1639294174659" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="10104" width="32" height="32"><path d="M168 640a40 40 0 0 0-40 40v240a40 40 0 0 0 80 0V680a40 40 0 0 0-40-40z m229.33-256a40 40 0 0 0-40 40v496a40 40 0 0 0 80 0V424a40 40 0 0 0-40-40zM856 384a40 40 0 0 0-40 40v496a40 40 0 0 0 80 0V424a40 40 0 0 0-40-40zM626.67 576a40 40 0 0 0-40 40v304a40 40 0 0 0 40 40 40 40 0 0 0 40-40V616a40 40 0 0 0-40-40zM920.33 64H776a40 40 0 0 0 0 80h47.5L631.14 334.11l-194.95-195a39.49 39.49 0 0 0-54.51-1.27q-0.82 0.74-1.62 1.53L75.72 443.76a39.85 39.85 0 0 0 56.36 56.36l276.17-276.18L600.34 416c0.69 0.82 1.4 1.61 2.16 2.38a39.59 39.59 0 0 0 20.82 11.12l0.54 0.09 1.16 0.2a39.42 39.42 0 0 0 33.18-10.5l0.61-0.58L880.33 199.9V248a40 40 0 0 0 80 0V104a40 40 0 0 0-40-40z" fill="#999999" p-id="10105"></path></svg>
                            </el-tooltip>
                          </div>
                          <br><br>
                        </div>
                      </div>
                    </div>
                  </el-col>
                  <div v-if="analysis === false">
                    <el-col class="noPrint"  :span="7">
                      <div class="pcontent_con">
                        <div class="pcontent">
                          <div v-if="activateContent === -1">
                            <el-row type="flex" class="wgdheader" justify="center" style="background-color: #f8f8f8;height: 115px;">
                              <el-col :span="8">
                                <div style="display: flex;justify-content: center;height: 106px;align-content: center">
                                  <span style="font-size: 20px;color: dimgrey;display: flex; align-items: center;">未归档</span>
                                </div>
                              </el-col>
                            </el-row>
                            <div class="articleEmpty" v-if="notArchivedBlog.length === 0"></div>
                            <div v-else>
                              <div class="article_con" v-for="(item,index) in notArchivedBlog" :key="index">
                                <div @click="show(item)" >
                                  <p class="red" style="font-size: 16px;">{{item.title}}</p>
                                  <p style="color: #9B9B9B">{{item.createTime.substring(0,11)}}</p>
                                  <p>{{item.content.substring(0,120)}}</p>
                                </div>

                                <br>
                                <p style="font-size: 18px;">
                                  <el-row type="flex" class="row-bg" justify="space-between">
                                    <el-col :span="4">
                                      <el-tooltip class="item" effect="dark" content="新界面打开" placement="bottom-start">
                                        <i class="el-icon-position" @click="openInNew(item)"></i>
                                      </el-tooltip>
                                    </el-col>
                                    <el-col :span="4">
                                      <el-tooltip class="item" effect="dark" content="重新编辑" placement="bottom-start">
                                        <i class="el-icon-edit"  @click="reEditor(item)"></i>
                                      </el-tooltip>
                                    </el-col>
                                    <el-col :span="4">
                                      <el-tooltip class="item" effect="dark" content="删除" placement="bottom-start">
                                        <i class="el-icon-delete" @click="deleteArticle(item)"></i>
                                      </el-tooltip>
                                    </el-col>
                                    <el-col :span="4">
                                      <el-tooltip class="item" effect="light" placement="bottom-start">
                                        <i class="el-icon-folder"></i>
                                        <div slot="content">
                                          <div v-if="dirs.length === 0">暂无可移动文件夹</div>
                                          <div v-else>
                                            <el-row>
                                              <span>移动到：</span><br>
                                            </el-row>
                                            <el-row v-for="(it,ind) in dirs" :key="ind" >
                                              <el-button @click="removeArticle(item,it)" type="text">{{it.name}}</el-button>
                                            </el-row>
                                          </div>
                                        </div>
                                      </el-tooltip>
                                    </el-col>
                                    <el-col :span="4">
                                      <el-tooltip class="item" effect="dark" content="打印 pdf" placement="bottom-start">
                                        <svg @click="printArticle(item)" t="1639288664411" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="4463" width="32" height="32"><path d="M341.333333 640v170.666667h384v-170.666667H341.333333z m-42.666666 42.666667H170.666667V341.333333h128V170.666667h469.333333v170.666666h128v341.333334h-128v170.666666H298.666667v-170.666666z m42.666666-298.666667H213.333333v256h42.666667v-42.666667h554.666667v42.666667h42.666666V384H341.333333z m0-42.666667h384V213.333333H341.333333v128z m-85.333333 85.333334h128v42.666666H256v-42.666666z" fill="#444444" p-id="4464"></path></svg>
                                      </el-tooltip>
                                    </el-col>
                                  </el-row>
                                </p>
                              </div>
                            </div>
                          </div>
                          <div v-if="activateContent === 1">
                            <el-row type="flex" class="wgdheader" justify="center" style="background-color: #f8f8f8">
                              <el-col :span="8">
                                <div style="display: flex;justify-content: center;height: 106px;align-content: center">
                                  <span style="font-size: 20px;color: dimgrey;display: flex; align-items: center;">文件夹</span>
                                </div>
                              </el-col>
                              <el-col :span="6">
                                <div style="padding: 40px 20px">
                                  <el-tooltip class="item" effect="dark" content="新建文件夹" placement="bottom">
                                    <svg  @click="addDir()" t="1639293957300" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="9035" width="32" height="32"><path d="M475.356 261.395L364.101 128.066H64.998v166.619H65V895.64h895.354V261.395H475.356z m313.079 634.244c-53.354 0-101.031-24.942-132.564-64.081a169.998 169.998 0 0 1-1.969-2.497H129.881V327.974h765.593v253.202l0.035 0.028c39.515 32.323 64.845 82.132 64.845 138.018 0.001 97.433-76.97 176.417-171.919 176.417z" fill="#839495" p-id="9036"></path><path d="M895.509 581.204l-0.035-0.028c-29.37-24.012-66.568-38.37-107.039-38.37-94.948 0-171.919 78.985-171.919 176.417 0 41.528 13.988 79.701 37.386 109.839 0.65 0.838 1.304 1.672 1.969 2.497 31.533 39.14 79.21 64.081 132.564 64.081 94.948 0 171.919-78.985 171.919-176.417 0.001-55.887-25.33-105.696-64.845-138.019z m17.387 167.041h-100.13v102.75h-48.661v-102.75h-100.13v-49.934h100.13v-102.75h48.661v102.75h100.13v49.934z" fill="#287CB0" p-id="9037"></path><path d="M812.766 698.312v-102.75h-48.661v102.75h-100.13v49.933h100.13v102.75h48.661v-102.75h100.13v-49.933z" fill="#FFFFFF" p-id="9038"></path></svg>
                                  </el-tooltip>
                                </div>
                              </el-col>
                            </el-row>

                            <div class="articleEmpty" v-if="dirs.length === 0"></div>
                            <div v-else>
                              <div class="article_con" style="line-height: 26px" v-for="(item,index) in dirs" :key="index">
                                <div>
                                  <el-row>
                                    <el-col :span="20" >
                                      <div @click="showDirContent(item)">
                                        <p style="font-size: 16px;">{{item.name}}</p>
                                        <p>{{item.count}}  条博文</p>
                                      </div>
                                    </el-col>
                                    <el-col :span="4">
                                      <i @click="deleteDir(item)" class="delete-dir el-icon-delete"></i>
                                    </el-col>
                                  </el-row>
                                </div>
                              </div>
                            </div>
                          </div>
                          <div v-if="activateContent === 0">
                            <el-row style="height: 108px;padding: 24px">
                              <el-input  @input="getSearchResults()" class="sipu" v-model="sInput" placeholder="请输入内容"></el-input>
                            </el-row>
                            <el-row>
                              <span style="padding-left: 24px">正在搜索</span> &nbsp;&nbsp;
                              <svg t="1638967653880" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="3241" width="16" height="16"><path d="M988.5 1017H155.6V7h832.9v1010z m-816.9-16h800.9V23H171.6v978z" fill="" p-id="3242"></path><path d="M163.6 15h211.6v994H163.6z" fill="#F76B8A" p-id="3243"></path><path d="M383.2 1017H155.6V7h227.6v1010z m-211.6-16h195.6V23H171.6v978z" fill="" p-id="3244"></path><path d="M688.1 406.1h186.3v16H688.1zM688.1 504h186.3v16H688.1zM688.1 601.9h186.3v16H688.1z" fill="#66BFBF" p-id="3245"></path><path d="M43.4 169.8h207.7v16H43.4zM43.4 303.5h207.7v16H43.4zM43.4 437.2h207.7v16H43.4zM43.4 570.8h207.7v16H43.4zM43.4 704.5h207.7v16H43.4zM43.4 838.2h207.7v16H43.4z" fill="" p-id="3246"></path></svg>
                              &nbsp;
                              <el-tooltip class="item" effect="light" placement="bottom">
                                <div slot="content">
                                  <el-button @click="searchMode='全部'" type="text">全部</el-button>

                                  <el-row v-for="(item,index) in dirs" :key="index" >
                                    <el-button @click="alertSearchMode(item)" type="text">{{item.name}}</el-button>
                                  </el-row>
                                  <br>
                                </div>
                                <el-button type="text">{{searchMode}} <i class="el-icon-arrow-down"></i></el-button>
                              </el-tooltip>
                              <br>
                              <hr>
                              <div class="articleEmpty" v-if="sResult.length === 0"></div>
                              <div v-else>
                                <div class="article_con" v-for="(item,index) in sResult" :key="index">
                                  <div @click="show(item)" >
                                    <p class="red" style="font-size: 16px;">{{item.title}}</p>
                                    <p style="color: #9B9B9B">{{item.createTime.substring(0,11)}}</p>
                                    <p>{{item.content.substring(0,120)}}</p>
                                  </div>

                                  <br>
                                  <p style="font-size: 18px;">
                                    <el-row type="flex" class="row-bg" justify="space-between">
                                      <el-col :span="4">
                                        <el-tooltip class="item" effect="dark" content="新界面打开" placement="bottom-start">
                                          <i class="el-icon-position" @click="openInNew(item)"></i>
                                        </el-tooltip>
                                      </el-col>
                                      <el-col :span="4">
                                        <el-tooltip class="item" effect="dark" content="重新编辑" placement="bottom-start">
                                          <i class="el-icon-edit"  @click="reEditor(item)"></i>
                                        </el-tooltip>
                                      </el-col>
                                      <el-col :span="4">
                                        <el-tooltip class="item" effect="dark" content="删除" placement="bottom-start">
                                          <i class="el-icon-delete" @click="deleteArticle(item)"></i>
                                        </el-tooltip>
                                      </el-col>
                                      <el-col :span="4">
                                        <el-tooltip class="item" effect="light" placement="bottom-start">
                                          <i class="el-icon-folder" @click="removeArticle(item)"></i>
                                          <div slot="content">
                                            <div v-if="dirs.length === 0">暂无可移动文件夹</div>
                                            <div v-else>
                                              <el-row>
                                                <span>移动到：</span><br>
                                              </el-row>
                                              <el-row v-for="(it,ind) in dirs" :key="ind" >
                                                <el-button @click="removeArticle(item,it)" type="text">{{it.name}}</el-button>
                                              </el-row>
                                            </div>
                                          </div>
                                        </el-tooltip>
                                      </el-col>
                                      <el-col :span="4">
                                        <el-tooltip class="item" effect="dark" content="打印 pdf" placement="bottom-start">
                                          <svg @click="printArticle(item)" t="1639288664411" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="4463" width="32" height="32"><path d="M341.333333 640v170.666667h384v-170.666667H341.333333z m-42.666666 42.666667H170.666667V341.333333h128V170.666667h469.333333v170.666666h128v341.333334h-128v170.666666H298.666667v-170.666666z m42.666666-298.666667H213.333333v256h42.666667v-42.666667h554.666667v42.666667h42.666666V384H341.333333z m0-42.666667h384V213.333333H341.333333v128z m-85.333333 85.333334h128v42.666666H256v-42.666666z" fill="#444444" p-id="4464"></path></svg>
                                        </el-tooltip>
                                      </el-col>
                                    </el-row>
                                  </p>
                                </div>
                              </div>
                            </el-row>
                          </div>
                          <div v-if="activateContent === 9">
                            <el-row type="flex" class="wgdheader" justify="center" style="background-color: #f8f8f8">
                              <el-col :span="20">
                                <div style="display: flex;justify-content: center;height: 106px;align-content: center">
                                  <span style="font-size: 20px;color:dimgrey;display: flex; align-items: center;">{{currentDir}}</span>
                                </div>
                              </el-col>
                            </el-row>

                            <div class="articleEmpty" v-if="dirsDetails.length === 0"></div>
                            <div v-else>
                              <div class="article_con" v-for="(item,index) in dirsDetails" :key="index">
                                <div @click="show(item)" >
                                  <p class="red" style="font-size: 16px;">{{item.title}}</p>
                                  <p style="color: #9B9B9B">{{item.createTime.substring(0,11)}}</p>
                                  <p>{{item.content.substring(0,120)}}</p>
                                </div>

                                <br>
                                <p style="font-size: 18px;">
                                  <el-row type="flex" class="row-bg" justify="space-between">
                                    <el-col :span="4">
                                      <el-tooltip class="item" effect="dark" content="新界面打开" placement="bottom-start">
                                        <i class="el-icon-position" @click="openInNew(item)"></i>
                                      </el-tooltip>
                                    </el-col>
                                    <el-col :span="4">
                                      <el-tooltip class="item" effect="dark" content="重新编辑" placement="bottom-start">
                                        <i class="el-icon-edit"  @click="reEditor(item)"></i>
                                      </el-tooltip>
                                    </el-col>
                                    <el-col :span="4">
                                      <el-tooltip class="item" effect="dark" content="删除" placement="bottom-start">
                                        <i class="el-icon-delete" @click="deleteArticle(item)"></i>
                                      </el-tooltip>
                                    </el-col>
                                    <el-col :span="4">
                                      <el-tooltip class="item" effect="light" placement="bottom-start">
                                        <i class="el-icon-folder" @click="removeArticle(item)"></i>
                                        <div slot="content">
                                          <div v-if="dirs.length === 0">暂无可移动文件夹</div>
                                          <div v-else>
                                            <el-row>
                                              <span>移动到：</span><br>
                                            </el-row>
                                            <el-row>
                                              <el-button @click="removeArticle(item,{id:0})" type="text">未归档</el-button>
                                            </el-row>
                                            <el-row v-for="(it,ind) in dirs" :key="ind" >
                                              <el-button @click="removeArticle(item,it)" type="text">{{it.name}}</el-button>
                                            </el-row>
                                          </div>
                                        </div>
                                      </el-tooltip>
                                    </el-col>
                                    <el-col :span="4">
                                      <el-tooltip class="item" effect="dark" content="打印 pdf" placement="bottom-start">
                                        <svg @click="printArticle(item)" t="1639288664411" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="4463" width="32" height="32"><path d="M341.333333 640v170.666667h384v-170.666667H341.333333z m-42.666666 42.666667H170.666667V341.333333h128V170.666667h469.333333v170.666666h128v341.333334h-128v170.666666H298.666667v-170.666666z m42.666666-298.666667H213.333333v256h42.666667v-42.666667h554.666667v42.666667h42.666666V384H341.333333z m0-42.666667h384V213.333333H341.333333v128z m-85.333333 85.333334h128v42.666666H256v-42.666666z" fill="#444444" p-id="4464"></path></svg>
                                      </el-tooltip>
                                    </el-col>
                                  </el-row>
                                </p>
                              </div>
                            </div>

                          </div>
                        </div>
                      </div>
                    </el-col>
                    <el-col class="needPrint"  :span="14">
                      <div class="info">
                        <h1 style="color: #000">{{showItem.title}}</h1>
                        <br>
                        <span style="font-size: 16px">作者：{{pdetails.author}}</span> &nbsp;&nbsp;
                        <span style="font-size: 16px">发表时间：{{showItem.createTime.substring(0,11)}}</span>
                        <br><br>
                        <el-row class="tagsGroup">
                          <el-tag size="small" type="danger">{{pdetails.dir}}</el-tag>
                          <div style="display: inline-block" v-if="pdetails.tags.split(',').length>0">
                            <el-tag
                              v-for="(tag,index) in pdetails.tags.split(',')"
                              :key="index"
                              :type="type[(index % 5)]">
                              {{ tag }}
                            </el-tag>
                          </div>
                        </el-row>
                        <br>
                        <hr>
                        <br>
                      </div>

                      <div class="adjustHeight" style="height: 78vh; float: left; width:100%;padding: 10px">
                        <mavon-editor
                          ref="md"
                          :value="showContents"
                          :defaultOpen="'preview'"
                          style="height: 78vh"
                          class="adjustHeight"
                          :previewBackground="'inherit'"
                          :toolbarsFlag="false"
                          :editable="false"
                          :boxShadow="false"
                          :scrollStyle="false"
                          :boxShadowStyle="'0'"
                          :subfield = "false"
                          :ishljs="true"
                          :codeStyle="'idea'"
                          :externalLink="externalLink"
                        />
                      </div>
                    </el-col>
                  </div>
                  <div v-else>
                    <el-col :span="20" style="padding: 40px 0 20px 40px">
                      <Echarts/>
                    </el-col>
                  </div>
                </el-row>
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
    import Echarts from "./Echarts"

    import {
      addDir,
      deleteArticle, deleteDir,
      getDirs,
      getDirsDetails,
      getNotArchived, getPrintDetails, removeArticle,
      searchByTitleAndDir
    } from "../../network/publish";
    import {getMDs} from "../../network/articleDetails";
    import axios from "axios";

    let loginCheck;
    export default {
      name: "published",
      components:{Nav,Header,Echarts},
      data(){
          return{
            activateContent:-1,
            notArchivedBlog:[],
            analysis:false,
            searchMode:"全部",
            sInput:'',
            sDir:-1,
            sResult:[],
            externalLink: {
              markdown_css: function() {
                // 这是你的 markdown css文件路径
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
            },
            showContents:'',
            dirs:[],
            currentDir:"",
            dirsDetails:[],
            showItem:{
              createTime:''
            },
            pdetails:{
              dir:'',
              tags:''
            },
            type:['success','','warning','danger','info'],
            canDownload:false,
          }
      },
      methods:{
        openAnalysis(){
          this.analysis=true
        },
        deleteDir(item){
          this.$confirm('删除文件夹 【 '+item.name+' 】 后，文章将被移至【未归档】，是否继续？', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            deleteDir(parseInt(item.id)).then(res=>{
              if(res.data.code === 20000){
                this.$message.success("删除成功")
                for(let i=0;i<this.dirs.length;i++){
                  if(this.dirs[i].id === item.id){
                    this.dirs.splice(i,1);
                    break;
                  }
                }
              }
            })
          }).catch(() => {});
        },
        reEditor(item){
          let editorStatus = localStorage.getItem("editorStatus") || "";
          if(editorStatus.length>2){
            this.$confirm('当前有文章正在编辑，继续会覆盖编辑栏内容, 是否继续?', '提示', {
              confirmButtonText: '确定',
              cancelButtonText: '取消',
              type: 'warning'
            }).then(() => {
              deleteArticle(item.id).then(res =>{
                if(res.data.code === 20000){
                  localStorage.setItem("editorStatus","reEditor")
                  localStorage.setItem("editorID",item.id)
                  localStorage.setItem("needLoad","true")
                  this.$router.push('/nblog/publish')
                }
              })
            }).catch(() => {});
          }else{
            deleteArticle(item.id).then(res =>{
              if(res.data.code === 20000){
                localStorage.setItem("editorStatus","reEditor")
                localStorage.setItem("editorID",item.id)
                localStorage.setItem("needLoad","true")
                this.$router.push('/nblog/publish')
              }
            })
          }
        },
        printArticle(item){
          let _this =this
          this.show(item)
          let inter = setInterval(()=>{
            if(_this.canDownload){
              _this.canDownload = false;
              clearInterval(inter);
              setTimeout(()=>{
                window.print();
              },200)

            }
          },10)
        },
        addDir(){
          this.$prompt('给你的文件夹起个名字吧', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            inputPattern: /^[0-9a-zA-Z\u4e00-\u9fa5]{1,15}$/,
            inputErrorMessage: '只能是中文数字或字母，长度为 1-15'
          }).then(({ value }) => {
            addDir(value).then(res=>{
              if(res.data.code === 20000){
                this.$message({
                  type: 'success',
                  message: '文件夹创建成功'
                });

                getDirs().then(res =>{
                  if(res.data.code === 20000){
                    this.dirs=res.data.data
                  }
                })
              }
            })

          }).catch(() => {});
        },
        removeArticle(item,it){
          let aid = parseInt(item.id);
          let did = parseInt(it.id)

          if(it.name === this.currentDir){
            this.$message({
              type: 'success',
              message: '移动成功'
            });
          }else {
            removeArticle(aid,did).then(res=>{
              if(res.data.code === 20000){
                this.updateArray(item)
                this.$message({
                  type: 'success',
                  message: '移动成功'
                });
              }else {
                this.$message({
                  type: 'error',
                  message: res.data.data
                });
              }
            })
          }
        },
        deleteArticle(item){
          this.$confirm('此操作将永久删除 [ '+item.title+' ], 是否继续?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            deleteArticle(item.id).then(res =>{
              if(res.data.code === 20000){
                if(this.activateContent!==0){
                  this.updateArray(item)
                }else {
                  for(let i=0;i<this.sResult.length;i++){
                    if(this.sResult[i].id === item.id){
                      this.sResult.splice(i,1);
                      break;
                    }
                  }
                }
                this.$message({
                  type: 'success',
                  message: '删除成功!'
                });
              }else {
                this.$message({
                  type: 'error',
                  message: res.data.data
                });
              }
            })

          }).catch(() => {});
        },
        updateArray(item){
          if(this.activateContent === -1){
            for(let i=0;i<this.notArchivedBlog.length;i++){
              if(this.notArchivedBlog[i].id === item.id){
                this.notArchivedBlog.splice(i,1);
                break;
              }
            }
          }else if(this.activateContent === 9){
            for(let i=0;i<this.dirsDetails.length;i++){
              if(this.dirsDetails[i].id === item.id){
                this.dirsDetails.splice(i,1);
                break;
              }
            }
          }
        },
        openInNew(item){
          const encode=(item.id*99)^0xBEEF;
          this.$router.push("/nblog/article/"+encode+".html")
        },
        publish(){
          this.$router.push('/nblog/publish')
        },
        getSearchResults(){
          if(this.sInput.length>0){
            if(this.searchMode === "全部"){
              this.sDir=-1
            }
            searchByTitleAndDir(this.sInput,this.sDir).then(res =>{
              if(this.sInput.length>0) {
                this.sResult = res.data.data
              }else {
                this.sResult = []
              }
            }).catch(()=>{})
          }else {
            this.sResult = []
          }
        },
        alertSearchMode(item){
          this.sDir = item.id;
          this.searchMode = item.name

          if(this.sInput.length>0){
            if(this.searchMode === "全部"){
              this.sDir=-1
            }

            searchByTitleAndDir(this.sInput,this.sDir).then(res =>{
              this.sResult = res.data.data
            }).catch(()=>{})

          }else {
            this.sResult = []
          }
        },
        showDirContent(item){
          getDirsDetails(item.id).then(res =>{
            if(res.data.code === 20000){
              this.currentDir = item.name;
              this.dirsDetails = res.data.data;
              this.activateContent = 9;
            }
          }).catch(()=>{})
        },
        alertShow(item){
          this.analysis = false
          if(item === -1){
            getNotArchived().then(res =>{
              if(res.data.code === 20000){
                this.notArchivedBlog = res.data.data
                this.currentDir = "未归档";
              }
            }).catch(err => {})
          }

          if(item === 1 || item === 0){
            getDirs().then(res =>{
              if(res.data.code === 20000){
                this.dirs=res.data.data
              }
            })
          }
          localStorage.setItem("pActivate",item)
          this.activateContent = item
        },
        show(item){
          if(localStorage.getItem("token").length<5){
            this.$message.info("请先登录")
            this.$router.push('/')
          }else{

            document.getElementsByClassName("v-show-content")[0].scrollTop = 0

            let user =  JSON.parse(localStorage.getItem("nbUser"))
            axios.defaults.headers = {}
            getMDs(user.id,item.author).then(res =>{
              axios.defaults.headers = {
                "Content-Type": "application/x-www-form-urlencoded;charset=UTF-8",
              }
              axios.defaults.headers.token = localStorage.getItem("token")

              this.showItem = item
              this.showContents = res.data
              getPrintDetails(parseInt(item.id)).then(res=>{
                if(res.data.code === 20000){
                  this.pdetails = res.data.data
                  this.canDownload = true
                }
              })
              this.alertUrl()
            }).catch(()=>{
              axios.defaults.headers = {
                "Content-Type": "application/x-www-form-urlencoded;charset=UTF-8",
              }
              axios.defaults.headers.token = localStorage.getItem("token")
            })
          }
        },
        alertUrl(){
          let user =  JSON.parse(localStorage.getItem("nbUser"))
          let serverPath = 'http://nblog.org.cn/images/'
          let _this = this;
          // 匹配博文中所有图片，[]兜底防止匹配不到
          let fakeUrls = _this.showContents.match(/!\[(.*?)\]\((.*?)\)/g) || [];
          // backUrl替换为realUrl，以正确显示博文图片
          fakeUrls.forEach(function(item) {
            let url = item.substring(item.lastIndexOf("(")+1,item.indexOf(")"))
            _this.$refs.md.$imgUpdateByUrl(url, serverPath + user.id + "/" + url +".png");
          });
        },
      },
      created() {
        let activate= localStorage.getItem("pActivate") || "-1"
        this.activateContent = parseInt(activate);

        getNotArchived().then(res =>{
          if(res.data.code === 20000){
            this.notArchivedBlog = res.data.data
          }
        }).catch(err => {})

        getDirs().then(res =>{
          if(res.data.code === 20000){
            this.dirs=res.data.data
          }
        })
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
  .delete-dir{
    font-size: 20px;
  }
  .delete-dir:hover{
    color: red;
  }
  .tagsGroup span{
    position: relative;
    margin: 2px;
  }
  .info{
    padding-left: 20px;
  }
  .sipu{
    height: 100%;
    width: 100%;
  }



  .article_con{
    padding: 10px;
    line-height: 25px;
    border-bottom: 1px #8c939d60 solid;
  }

  .article_con:hover{
    background-color: #5c637010;
  }

  .published_main{
    width: 1500px;
    padding: 0 120px 0 120px;
    position: relative;
    z-index: 100;
  }

  .published_card{
    height: 80vh;
    width: 100%;
    background-color: #fff;
  }

  .pcontent_con{
    height: 80vh;width: 100%; float: left;padding-left: 1px;
    overflow: scroll;
    overflow-x:hidden;
    -ms-overflow-x-style: none;
    scrollbar-width: none; /* Firefox */
  }

  .pcontent_con::-webkit-scrollbar {
    overflow-x:hidden;
  }

  .info{
    display: none;
  }
  .pnav{
    display: flex;
    justify-content: center;
    height: 100%;
    width: 100%;
    background-color: #f8f8f8;
    border-right: 1px solid #ececec
  }

  .pcontent{
    height: 100%;
    width: 100%;
  }

  .pnav_main{
    padding: 20px 13px 20px 17px;
  }

  .pnav_icon img{
    margin-left: -5px;
    width: 50px;
    height: 36px;
  }

  .pnav_file img{
    width: 50px;
    height: 50px;
  }

  .articleEmpty{
    width: 100%;
    height: 60vh;
    background-size: cover;
    -webkit-background-size: cover;
    -o-background-size: cover;
    background: url("../../assets/img/nodata.png") no-repeat center 0;
  }

  .fileimg{
    width: 36px;
    height: 36px;
    background: url("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACQAAAAkCAYAAADhAJiYAAAAg0lEQVR42mNgGAWjYBSMglFAHtDdl+ivuy/hORD/JxE/B+mlgYPIcgzcUbRw0H9K8MhzkML+BA58eDSEBp2DqK1+NMpGo2w0yoixAC6/P66cYb8DC41rexJCA+gYFLH98c0D6yCU0KKBY0htfsAdRCvHkNpAA6unpWNGwSgYBaNgJAEAAps4GapcyPIAAAAASUVORK5CYII=") no-repeat;
    background-size: cover;
  }

  .fileimg:hover{
    background: url("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACQAAAAkCAYAAADhAJiYAAAB90lEQVR42s2YvU7DMBCAPbD0Abp1AsYEIfECSPAK3VCdIiFl4lUQI6Jzh24sLKUxA0IVW1myIPEAFYiRSFXCndPmh0axndqtLZ1UtWf76519dz5CFIc77u87E893J3QIMnMCOncD7xeFf4bv8DeuA7rEyGCne85T7wI2fAGJYfNERlCXz4G5uIYWFnfcO3cDGspC1EiIazUG6Yy6Lfh3A1go1gCzErTYoPPabSnBHD5223AG3jSClF0Ja+Me8jBoXkMw+fnyQiEUmtKkZaosVes+h9H7bcFkULBnzW3SeoClD/pRQM/W4ozoal+/3ybz6CdRHTgH54pCQilOYeAS/ZMmMEUooesweGZAEE1FEzYdEq6b5rlJIh1sASh2JvSA8CQocfj+j5Pnq1ppAIRhwCc8azcAMmChBFkI3K6ZNUDAQpb1jDKQbv2lfKHLIotctrANKLLKZchi36G27to7rFlgrNO5+Rglx+yySX3kG0kdCFMcd58P8qkjTa6e1uRa1JeEyZOrifJjBaQAUy4/0gLN01agob4KzFqBxssQtrsSFveuLvLTh6EdRf5OnkFM8Aza/kOxL/t67beR3uxTWhKm5D5bmg0Vt09PO4Zt0I6palhhNFW0GOpOtTas1kID5JuspcdLF/oNmy6wuEprq7yll+UmhfEH20L3SIG6FE8AAAAASUVORK5CYII=") no-repeat;
  }

  .addimg{
    width: 36px;
    height: 36px;
    background: url("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACQAAAAkCAYAAADhAJiYAAAB3klEQVR42s2YS07DMBBAs2PNGVizRhwFJBB3YMcarsSSXgBO0Eqo6ScpaWkWUVWpaYZ5Ea34ldqJ03gkS1Vqz7w445nxBIGlTKfTs/F4fKejMxwO33WswjAsGPzmGf8xh7lBEyIiR1EU3Y5Go9fBYCBqSLIsk+VyKXmeS1EU5eA3z/iPOcxlDWvR4QRG3/RS3/otjmNZLBalYVNhLmtYiw50VQZJ0/RYFTzqG5ZK6wo60IVOdFvBTCaTE93ubpIksl6vxZWgC53oxoYNTDSfz6UpQTc29kKxldA3CbMRtYVfdf/9fHxftvRQgi1s7jxNOJ1LnzHxKWzqKbz4FWc4li5OU5XTh+1vcYrARayoKlfP9+WoKtiGYQtENK2zO6dPN+WoGaPCbW4ixNtEYNdA2IZhNpudB59JsJYf1AVCYIAFoA7JsG0gGGAJKBfI0G0DwQALQCvKhraBYFA/ygMKKxOH5lhvDNsOk5AAAywHAbp+eTAH8umTweKfU3t37L0LjN6lDu+Sq6vyw+RoG5cf3hVobZewOy+QXhX5bVyD1FZv7y2Wy5vmlJgFTcJgw/b22vPiKu1ls6GJdozuSlKrHbOjYRVWaFiFThtWP4V887WlR9nZ7/flr5beNjdZyAf9qfmC3LddBwAAAABJRU5ErkJggg==");
    background-size: cover;
  }

  .addimg:hover{
    background: url("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACQAAAAkCAYAAADhAJiYAAABvElEQVR42s1YzUrDQBDeg5c+QG89qceNzyDoK+Qm3c2t72TPHnwALzXZi0gRRHoK+gqi9GihRGc2NiE0pJPsJp2FgdBsZr7O/4wQLU+wiE5lrGdBrO6AVjJRn0Gif5DsM/yG7+wduCt6OebyRD5Ob0DgE1AGwn8phHftN/At8vCCJVhMr4NEpVQQDZQir85AJvfhCP7dHBhlHsDsCDU2nzyHo1Zgzh/CMfjAi0cgVVMCb5RBB4Pq7QlM6V86PQgKVdmnZuo01Wg+adTtUGAKUCCzIZq8OjDZ0S8SdbWXZzyFdueUUMlTmLhcGL6tPyw5mQ6TZwEIsqkLs91x1NKyrE0tykGPgDIZqzNhi6CjD3gChGlgJmzVZgIIsQiIrhUbQIBF/PczTADpLzTZhhraXQ85JcRqOwig1/U7FdCGlckQCz+nZhf20nBLjNxKR15cNZ/i6qv9IIc2pf3IGzTNp0GzbYg5XguLsuub/Hww5NHkH2UMMgfGoOEHxYg6vUZjRN/vKE0EUzEfl2VDTfT5WccYh3VM3cIKs2lLjeHdpdeF1V5qgHpTrPRs66K+QegWm6u8typXekVtanH+ABqtwq/7bw5UAAAAAElFTkSuQmCC");
  }

  .searchimg{
    width: 36px;
    height: 36px;
    background-size: cover;
    background: url("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACQAAAAkCAYAAADhAJiYAAAC00lEQVR42s2Yu24TQRSGt6PmGahR6BAF8AIImhSECigQIKChQEQKErRcGl6AF6BCSoXiwkKEQOESKUGO15ddxzZ2YSwj7+5hviVYcYJ356y9iX9ptZc5c86/M+cyM46jRLvdPt9oNFbNVajVaj/NNXJdN+LimW+0IYOskwdE5JTneY/r9Xq5Wq2KMST9fl+Gw6EEQSBRFMUXz3yjDRlk6UNfdMyFjPnTG+av93zfl8FgEBu2BbL0oS860JWZSK/XO20UrJs/jJXOCnSgC53oVpFpNptnzHBvt1otCcNQ5gV0oRPd2NCQ8brdruQFdGMjlRRDCfs8yfyDsYVfbSdOH/PLkB4XsIXNqdGE083TZ2x8CpsmCleO5BnC0iaagiiQD94nuVt6LReLD+Rc4XZ8553vtGujD9sTeYrERa5Iw+4vT5a3nsnZjZty5fMTefH9nbz98T6+88532stGTgNsw2FMiGyaNjqQuVR8KJeLj+Tj3rcjCZJ3vtOOnIbUfo5yx7WJFJ+UgZkG/hxjlUHySNKO3PLWmowspw/bcOh0Ohec/SKY2AHfYDoYARsghzz9bAEHuECoQDFMAg6Lj9jWMeSQp58t4AAXh+UCFToJRBGOqwHy9LMFHOACoRHLhiQsmdAmmjRAfmnjlrU8HIwfBQ4Lq7SpOI4RggNcrAhl9aH7pTd6QjZTljXK1v1N1ZTBxcqpR3EeWlPloZWvzyWMQr1T24Q9KCsyNaNz7cuqtH/39GFvkxgPkmKkkmoZI3N186ma1Dgx2pSOw9P3t9q/iqOI0OZ+zzgwPsM0QQIytqQmSodtcdVCQ2qiuGqWH7OQum6m0nr5oVmgZSEFmTull/YLtJNewk7dQC7UIv8ktkHG1k7qLpbNm6kpPh3yJIMN7e51ZyG20gt52JDHcYwZldZMxzFTDqzcDAdW7lwPrA6DenPwSI9lZ6VSkf8d6Y1rkwJ/ABuA2U2cNqFnAAAAAElFTkSuQmCC");
  }

  .red{
    color: black
  }

  .searchimg:hover{
    background: url("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACQAAAAkCAYAAADhAJiYAAACt0lEQVR42s1Yv0/bQBT2wMIfwMbUdrRp/4NWaifEmg1xSSemqktL+De6tAhmJNi6dKGxJQIk6tSQoizQUImqAwlSIio1KOT6vjM4SnHt94wNOenkxPfu7vN73/txZ1nC5mwVHtil/KJTUuvUa7arTh03/wfd/KZ3GDMyJGtl0rxnE/bnhXnacIf6gDbXnA5ZM4fmYo1UsDhbCy8cVzW4ICJ6A2slBjK9mZukr1ujhQYpgLnu0Nja9F5uUgTm0afcFHHgS4pARk1Ja2MPPhioNyMwQ37lG7GgoMosNROmqUjz2Z5avSswASjaM8KbUiUwm+gzrnp+I85wXfuJ91IXD1Z0ubWvz3pd3R9cmme5VTPvMZ4kJIzEKQQuzsS5ypJudH9otOPfv/TmiatXmh/NE//RMD5XKcpNh+AZAKJoygHTJk20eh39ev9dqAzeYxxyCUBVh7kpJh3ADPhybDZbeRu5MMYhB3mh+QZ2ST20TBKMEQY30P6nmTBNoWGeMAwsWiZrxwiCwOCIZHHIY57IbITFIu+qxQnCi0BcyeKQxzwZj1TNuqpnIgXh2h/ImySLQ/6S5gmJ3YbJemOjoZLqswAl4VCT5LdbX6WAeiyTJfWyN9/eCysAdcoidZI4VO8c6cduQU5qjtubSF0tGk5wIjXa4fmJflp+JXd724sPjMP0UQxyGTiyQcSFN+HZvMpl0MzR+c9EoPzAyEgd/5pv+TrbX3SNa0Nz25TtwRmYCSAARgjKTx1+cs3vpF3nJABVFZcftwFV73znlx9+gZZNYQ9QALPbrvMLNFOGePdXwmLv8CLfPxiOR5F/L8cgL+YYdPcHxQL39FqYAvpsj9JMMCPmG5fLhhDvS+c6xrvFdUzYhRWiqVBjkK2memF1IzRQvgmu9Ezpos5o0z6KK7+2Gl7pBblJ0P4Cl2zh4u7sM1MAAAAASUVORK5CYII=");
  }

  .dirimg{
    width: 36px;
    height: 36px;
    background-size: cover;
    background: url("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACQAAAAkCAYAAADhAJiYAAAAcElEQVR42mNgGAWjYBSMglFAHtDdl/AfhJHZROLnuvsS/QeTg8COGmwO+j9yHUSMnlEHDSoHjSbqUQeNOojaDiITPx9MDqJdba+zN8aKPL3xhbRw0HNyHKS1P9aGRlGW6A8J/kESZaNgFIyCUTBSAACoogXoPTWfWgAAAABJRU5ErkJggg==");
  }

  .dirimg:hover{
    background: url("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACQAAAAkCAYAAADhAJiYAAAB90lEQVR42s2YvU7DMBCAPbD0Abp1AsaEZ0CCV8gGcbJ14ilgYWOmcwYegKUkWRCqEAxlycAjVCBGIlUtdwlJiBLic+KktnRS1Prns+98dz7GJJs5d/cN35maPvdAlkbAV2bgfKMk3/Ab/pf0gb6slxYe7xkP9hks+AiygcW3FMG+yRgYi3MoYTHn9qkZ8IgK0SARztUaZHJnjWB3M5hoowAmEzyx2eTJGknBHN5bY7CBZ4UgZVXC3LgGHQaPtyeYwr6cSAiFR9nnydSdVKP6jJDfDgWTQ8GaDbdJqQGTDf0o4CcVPyO62ln7+01pq/hre/F2I3QJJT+Fjku0k7ZAGZRQdeg8cyDwpn0CZeMEsihiEyEcDAC0MXx+wJIgSDC+/4AoY4hA6AamLInamgAhC4PbtdQGCFjYbz6z1cCGUD5QZbE2QD5f6wYUa6UyZOls1GptCIy667VXrDIPUg45x9imUWJZ4RglQ0cbGEK0L0JHGlwdUnA9f7lslfNcv3v04EpNP3CnbYDs1yv59CNN0JoTezx2nLgnlUWVh6QZ7i6FxbXrk/z0YahHkr+TZ1AoeAYN/1B0qa9Xd4z0/T6liTAl9elSbKi5fWrKMWGHckxdwQq9qeSJYd+F0oJVxTVAvMlLeknqwj9h0TUmV2luVZT08tgk0X4ASkfZj4ss8JEAAAAASUVORK5CYII=");
  }

  @media print {
    .noPrint{
      display: none;
    }

    .needPrint{
      width: 100%;
    }

    .adjustHeight{
      height: auto !important;
    }

    .published_main{
      padding: 0;
      height: 100%;
      width: 100%;
    }
    .info{
      display: block;
    }
  }

</style>
