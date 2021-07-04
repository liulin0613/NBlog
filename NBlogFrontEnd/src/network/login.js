import {request} from "./request";

export function emailExist(email) {
  return request({
    url:'/user/emailExist',
    method:'post',
    data: {email}
  })
}

export function emailLogin(email) {
  return request({
    url:'/user/emailLogin',
    method:'post',
    data: {email}
  })
}

export function userLogin(name,pwd) {
  return request({
    url:'/user/userLogin',
    method:'post',
    data: {name,pwd}
  })
}

