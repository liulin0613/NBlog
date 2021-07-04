import {request} from "./request";

export function addfollow(name,fname) {
  return request({
    url:'user/addfollow',
    method:'post',
    data: {name,fname}
  })
}

export function hasfollow(name,fname) {
  return request({
    url:'user/hasfollow',
    method:'post',
    data: {name,fname}
  })
}

export function deletefollow(name,fname) {
  return request({
    url:'user/deletefollow',
    method:'post',
    data: {name,fname}
  })
}

export function getAllfollow(name) {
  return request({
    url:'user/getAllfollow',
    method:'post',
    data: {name}
  })
}

export function getPublishedByName(name) {
  return request({
    url:'user/getPublishedByName',
    method:'post',
    data: {name}
  })
}



