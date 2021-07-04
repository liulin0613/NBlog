import {request} from "./request";

export function Checkname(name) {
  return request({
    url:'/user/Checkname',
    method:'post',
    data: {name}
  })
}

export function emailExist(email) {
  return request({
    url:'/user/emailExist',
    method:'post',
    data: {email}
  })
}

export function emailYzm(email) {
  return request({
    url:'/user/emailYzm',
    method:'post',
    data: {email}
  })
}

export function addUser(name,pwd,email) {
  return request({
    url:'/user/addUser',
    method:'post',
    data: {name,pwd,email}
  })
}

