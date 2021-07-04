import {request} from "./request";

export function addfavorites(aid,name) {
  return request({
    url:'article/addfavorites',
    method:'post',
    data: {aid,name}
  })
}

export function hasfavorites(aid,name) {
  return request({
    url:'article/hasfavorites',
    method:'post',
    data: {aid,name}
  })
}

export function deletefavorites(aid,name) {
  return request({
    url:'article/deletefavorites',
    method:'post',
    data: {aid,name}
  })
}

export function getAllfavorites(name) {
  return request({
    url:'article/getAllfavorites',
    method:'post',
    data: {name}
  })
}


