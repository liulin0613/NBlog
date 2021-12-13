import {request} from "./request";

export function getArticles(page, limit) {
    return request({
        url: '/article/getArticlesByPagination',
        method: 'post',
        data: {page, limit},
    })
}

// 获取主页面热标签
export function getHotTags() {
  return request({
    url: '/tag/getHotTags',
    method: 'post',
  })
}


// 主页面侧边栏搜索博文，四种搜索类型
export function search(content, type) {
  return request({
    url: '/es/search',
    method: 'post',
    data: {content, type},
  })
}

// 推荐文章
export function getRecommendArticle() {
  return request({
    url: '/article/getRecommendArticle',
    method: 'post',
  })
}


// 增加访问量
export function addViews() {
  return request({
    url: '/user/addViews',
    method: 'post',
  })
}

// 获取访问量
export function getViews() {
  return request({
    url: '/user/getViews',
    method: 'post',
  })
}

