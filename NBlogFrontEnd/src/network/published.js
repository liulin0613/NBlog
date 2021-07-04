import {request} from "./request";

export function getPublished(name) {
  return request({
    url:'article/getPublished',
    method:'post',
    data: {name}
  })
}

export function deleteArticle(aid) {
  return request({
    url:'article/deleteArticle',
    method:'post',
    data: {aid}
  })
}

export function getEchartsData(name) {
  return request({
    url:'getEchartsData',
    method:'post',
    data: {name}
  })
}

