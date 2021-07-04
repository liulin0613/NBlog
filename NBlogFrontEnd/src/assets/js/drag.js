import Vue from 'vue';
//使用Vue.directive()定义一个全局指令
//1.参数一：指令的名称，定义时指令前面不需要写v-
//2.参数二：是一个对象，该对象中有相关的操作函数
//3.在调用的时候必须写v-
const drag = Vue.directive('drag', {
  //1.指令绑定到元素上回立刻执行bind函数，只执行一次
  //2.每个函数中第一个参数永远是el，表示绑定指令的元素，el参数是原生js对象
  //3.通过el.focus()是无法获取焦点的，因为只有插入DOM后才生效
  bind: function (el) { },
  //inserted表示一个元素，插入到DOM中会执行inserted函数，只触发一次
  inserted: function (el) {
    el.onmousedown = function (e) {
      var disx = e.pageX - el.offsetLeft;
      var disy = e.pageY - el.offsetTop;
      document.onmousemove = function (e) {
        el.style.left = e.pageX - disx + 'px';
        el.style.top = e.pageY - disy + 'px';
      }
      document.onmouseup = function () {
        document.onmousemove = document.onmouseup = null;
      }
    }
  },
  //当VNode更新的时候会执行updated，可以触发多次
  updated: function (el) { }
})
export default drag;
