import {request} from "./request";

export function getinfo(name) {
  return request({
    url:'/user/getinfo',
    method:'post',
    data: {name}
  })
}

export function alertName(oldname,name) {
  return request({
    url:'/user/alertName',
    method:'post',
    data: {oldname,name}
  })
}

export function alertPwd(name,pwd) {
  return request({
    url:'/user/alertPwd',
    method:'post',
    data: {name,pwd}
  })
}

