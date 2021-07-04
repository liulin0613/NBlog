import {request} from "./request";

export function getArticleData(page,limit,name) {
  return request({
    url:'article/getArticleData',
    method:'post',
    data: {page,limit,name}
  })
}

export function getHotTags() {
  return request({
    url:'getHotTags',
    method:'post',
  })
}

export function getAllTitles() {
  return request({
    url:'getAllTitles',
    method:'post',
  })
}
