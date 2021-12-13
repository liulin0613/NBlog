// 主页面侧边栏搜索博文，四种搜索类型
import {request} from "./request";

export function uploadImg(file) {
  return request({
    url: '/image/upload',
    method: 'post',
    data: {file},
  })
}

export function deleteImg(path) {
  return request({
    url: '/image/del',
    method: 'post',
    data: {path},
  })
}

export function addDraft(title,scope,content,tags) {
  return request({
    url: '/article/addDraft',
    method: 'post',
    data: {title,scope,content,tags},
  })
}

export function addArticle(title,scope,content,tags) {
  return request({
    url: '/article/addArticle',
    method: 'post',
    data: {title,scope,content,tags},
  })
}

export function getNotArchived() {
  return request({
    url: '/article/getNotArchived',
    method: 'post',
  })
}

export function getDirs() {
  return request({
    url: '/article/getDirs',
    method: 'post',
  })
}

export function getDirsDetails(aid) {
  return request({
    url: '/article/getDirsDetails',
    method: 'post',
    data:{aid}
  })
}

export function searchByTitleAndDir(title,did) {
  return request({
    url: '/article/searchByTitleAndDir',
    method: 'post',
    data:{title,did}
  })
}

export function deleteArticle(aid) {
  return request({
    url: '/article/deleteArticle',
    method: 'post',
    data:{aid}
  })
}

export function addDir(dirName) {
  return request({
    url: '/article/addDir',
    method: 'post',
    data:{dirName}
  })
}

export function removeArticle(aid,did) {
  return request({
    url: '/article/removeArticle',
    method: 'post',
    data:{aid,did}
  })
}

export function getPrintDetails(aid) {
  return request({
    url: '/article/printDetails',
    method: 'post',
    data:{aid}
  })
}

export function getRePublishArticle(aid) {
  return request({
    url: '/article/getRePublishArticle',
    method: 'post',
    data:{aid}
  })
}

export function reEditorSubmit(title,scope,content,tags,desc,aid) {
  return request({
    url: '/article/reEditorSubmit',
    method: 'post',
    data: {title,scope,content,tags,desc,aid},
  })
}

export function deleteDir(did) {
  return request({
    url: '/article/deleteDir',
    method: 'post',
    data:{did}
  })
}


export function getEchartsData() {
  return request({
    url: '/article/getEchartsData',
    method: 'post',
  })
}

export function getArticleInfo() {
  return request({
    url: '/user/getArticleInfo',
    method: 'post',
  })
}



