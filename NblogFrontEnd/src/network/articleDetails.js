import {request} from "./request";
import {staticRequest} from "./staticrequest";

export function getArticlesDetailsByID(id) {
  return request({
    url: '/article/getArticleDetailsByID',
    method: 'post',
    data: {id},
  })
}

export function getMDs(id,content) {
  return staticRequest({
    url: '/mds/'+id+"/"+content,
    method: 'get',
  })
}

// 添加关注一篇博文的作者
export function addFollow(fid) {
  return request({
    url: '/user/addFollow',
    method: 'post',
    data: {fid},
  })
}

// 取消关注一篇博文的作者
export function deleteFollow(fid) {
  return request({
    url: '/user/deleteFollow',
    method: 'post',
    data: {fid},
  })
}

// 添加收藏一篇博文
export function addFavorites(aid) {
  return request({
    url: '/user/addFavorites',
    method: 'post',
    data: {aid},
  })
}

// 取消收藏一篇博文
export function deleteFavorites(aid) {
  return request({
    url: '/user/deleteFavorites',
    method: 'post',
    data: {aid},
  })
}

// 点赞一篇博文
export function addLike(aid) {
  return request({
    url: '/user/addLike',
    method: 'post',
    data: {aid},
  })
}

// 取消点赞一篇博文
export function deleteLike(aid) {
  return request({
    url: '/user/deleteLike',
    method: 'post',
    data: {aid},
  })
}
