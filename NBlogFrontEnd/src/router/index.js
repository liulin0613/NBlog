import Vue from 'vue'
import VueRouter from 'vue-router'
const Editor=()=>import('views/editor/Editor')
const Home=()=>import('views/home/Home')
const Details=()=>import('views/details/Details')
const Login=()=>import('views/login/Login')
const Register=()=>import('views/register/Register')
const Published=()=>import('views/published/Published')
const Tag=()=>import('views/tag/Tag')
const TagDetails=()=>import('views/tagdetails/TagDetails')
const Favorites=()=>import('views/favorites/Favorites')
const Follow=()=>import('views/follow/Follow')
const Draft=()=>import('views/draft/Draft')
const FollowList=()=>import('views/follow/FollowList')
const Info=()=>import('views/info/Info')

Vue.use(VueRouter)

const routes = [
  {
    path:'/',
    redirect:'/nblog/login'
  },
  {
    path:'/nblog/login',
    component:Login,
    meta:{
      title:"登录"
    }

  },
  {
    path:'/nblog/register',
    component:Register,
    meta:{
      title:"注册"
    }
  },
  {
    path:'/nblog/new',
    component:Home,
    meta:{
      title:"最新分享"
    }
  },
  {
    path:'/nblog/publish/:class',
    component:Editor,
    meta:{
      title:"编辑"
    }
  },
  {
    path:'/nblog/published',
    component:Published,
    meta:{
      title:"已发表"
    }
  },
  {
    path:'/nblog/article/:class',
    component:Details,
    meta:{
      title:"详情"
    }
  },
  {
    path:'/nblog/tags',
    component:Tag,
    meta:{
      title:"标签"
    }
  },
  {
    path:'/nblog/tag/:tagname',
    component:TagDetails,
    meta:{
      title:"标签详情"
    }
  },
  {
    path:'/nblog/favorites',
    component:Favorites,
    meta:{
      title:"收藏"
    }
  },
  {
    path:'/nblog/follow',
    component:Follow,
    meta:{
      title:"关注"
    }
  },
  {
    path:'/nblog/draft',
    component:Draft,
    meta:{
      title:"草稿"
    }
  },
  {
    path:'/nblog/list',
    component:FollowList,
    meta:{
      title:"已发表"
    }
  },
  {
    path:'/nblog/info',
    component:Info,
    meta:{
      title:"个人信息"
    }
  }
]

const router = new VueRouter({
  // mode: 'history',
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
