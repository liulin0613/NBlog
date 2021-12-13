import Vue from 'vue'
import VueRouter from 'vue-router'
Vue.use(VueRouter)

const routes = [
  {
    path:'/',
    redirect:'/nblog/home'
  },
  {
    path: '/nblog/home',
    name: 'Home',
    component: () => import( '../views/home/Home.vue'),
    meta:{
      title:'首页'
    }
  },
  {
    path: '/nblog/article/:id',
    name: 'article',
    component: () => import( '../views/article/Article.vue'),
    meta:{
      title:'文章详情'
    }
  },
  {
    path: '/nblog/tag/:tag',
    name: 'tag',
    component: () => import( '../views/tag/Tag.vue'),
    meta:{
      title:'标签'
    }
  },
  {
    path: '/nblog/tags',
    name: 'tags',
    component: () => import( '../views/tag/AllTags.vue'),
    meta:{
      title:'标签'
    }
  },
  {
    path: '/nblog/publish',
    name: 'publish',
    component: () => import( '../views/publish/Publish.vue'),
    meta:{
      title:'发表文章'
    }
  },
  {
    path: '/nblog/favorite',
    name: 'favorite',
    component: () => import( '../views/favorite/Favorite.vue'),
    meta:{
      title:'收藏'
    }
  },
  {
    path: '/nblog/published',
    name: 'published',
    component: () => import( '../views/publish/Published.vue'),
    meta:{
      title:'已发表'
    }
  },
  {
    path: '/nblog/draft',
    name: 'draft',
    component: () => import( '../views/draft/Draft.vue'),
    meta:{
      title:'草稿'
    }
  },
  {
    path: '/nblog/info',
    name: 'info',
    component: () => import( '../views/info/Info.vue'),
    meta:{
      title:'个人信息'
    }
  },
  {
    path: '/nblog/focus',
    name: 'focus',
    component: () => import( '../views/focus/Focus.vue'),
    meta:{
      title:'关注'
    }
  },
  {
    path: '/nblog/list',
    name: 'focus',
    component: () => import( '../views/list/List.vue'),
    meta:{
      title:'发表列表'
    }
  },
]

const router = new VueRouter({
  base: process.env.BASE_URL,
  routes
})

router.beforeEach((to, from, next) => {
  /* 路由发生变化修改页面title */
  if (to.meta.title) {
    document.title = to.meta.title;
  }
  next();
})

export default router
