import {request} from "./request";

export function addArticle(aTitle,aTags,aScope,aContent,aAuthor) {
  return request({
    url:'article/add',
    method:'post',
    data: { aTitle, aTags, aScope, aContent , aAuthor}
  })
}

export function uploadimg(file) {
  return request({
    url:'markdown/uploadimg',
    method:'post',
    data: { file},
  })
}

export function getBase64ByUrl(name,time) {
  return request({
    url:'markdown/image/getBase64ByUrl',
    method:'post',
    data: {name,time},
  })
}

export function delImg(path) {
  return request({
    url:'markdown/delImg',
    method:'post',
    data: { path},
  })
}

export function getArticleInfo(aid) {
  return request({
    url:'article/getArticleInfo',
    method:'post',
    data: {aid},
  })
}

export function addDraft(aTitle,aTags,aScope,aContent,aAuthor) {
  return request({
    url:'article/addDraft',
    method:'post',
    data: { aTitle, aTags, aScope, aContent , aAuthor}
  })
}

export function getDraftInfo(did) {
  return request({
    url:'article/getDraftInfo',
    method:'post',
    data: {did},
  })
}

