import Vue from 'vue'
import Vuex from 'vuex'
import m1 from "./modules/m1";
import mutations from "./mutations";
import actions from "./actions";
import getters from "./getters";

Vue.use(Vuex)

const state={
  user:'',
  page:0,
  MDCont:'',
  MDTitle:'',
  MDScope:'公开',
  currentArticle:-1,
  reEditor:-1
}

export default new Vuex.Store({
  state,
  //对数据进行变化，同步操作
  mutations,
  //对数据进行变化，异步操作
  actions,
  //对数据进行预处理
  getters,
  //分模块
  modules: {
    m1
  }
})
