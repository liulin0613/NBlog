import {request} from "./request";

export function getAllTags() {
  return request({
    url:'getAllTags',
    method:'post',
  })
}

export function getTagArticleData(tagname,name) {
  return request({
    url:'getTagArticleData',
    method:'post',
    data: {tagname,name}
  })
}
