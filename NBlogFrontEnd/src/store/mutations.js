export default  {

  save (state,cont,title,scope) {
    state.MDCont=cont
    state.MDTitle=title
    state.MDScope=scope
  },

  clearMD(state){
    state.MDCont=''
    state.MDTitle=''
  },

  alertPage(state,page){
    state.page=page
    localStorage.setItem("PageStore",page)
  },

  username(state,name){
    state.currentUser=name
  },

  add(state,count){
    state.count=count
  },

  dev(state,count){
    state.count=count
  },

  alertArticle(state,aid){
    state.currentArticle=aid
  },

  alertname(state,name) {
    localStorage.setItem('user',name)
    state.user=name
  }
}
