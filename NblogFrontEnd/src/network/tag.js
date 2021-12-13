import {request} from "./request";

export function getAllTags() {
  return request({
    url: '/tag/getAllTags',
    method: 'post',
  })
}

export function getTagDetails(tagname) {
  return request({
    url: '/tag/getTagArticleData',
    method: 'post',
    data: {tagname},
  })
}
