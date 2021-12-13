import {request} from "./request";

export function checkExpiration() {
  return request({
    url: '/user/checkExpiration',
    method: 'post',
  })
}

export function exit() {
  return request({
    url: '/login/exit',
    method: 'post',
  })
}
