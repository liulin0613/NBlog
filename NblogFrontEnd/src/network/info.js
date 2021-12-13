// 推荐文章
import {request} from "./request";

export function getSocialInfo() {
  return request({
    url: '/user/getSocialInfo',
    method: 'post',
  })
}

export function alertAvatar(img) {
  return request({
    url: '/user/alertAvatar',
    method: 'post',
    data:{img}
  })
}

export function updateUserName(name) {
  return request({
    url: '/user/updateUserName',
    method: 'post',
    data:{name}
  })
}

export function updateUserIntro(intro) {
  return request({
    url: '/user/updateUserIntro',
    method: 'post',
    data:{intro}
  })
}

export function updateUserPwd(pwd) {
  return request({
    url: '/user/updateUserPwd',
    method: 'post',
    data:{pwd}
  })
}

export function getAllFollow() {
  return request({
    url: '/user/getAllFollow',
    method: 'post',
  })
}

export function getPublishedByID(id) {
  return request({
    url: '/user/getPublishedByID',
    method: 'post',
    data:{id}
  })
}


