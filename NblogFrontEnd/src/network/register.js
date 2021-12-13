import {request} from "./request";

export function checkName(name) {
  return request({
    url: '/login/checkIfTheUserNameExists',
    method: 'post',
    data: {name},
  })
}

export function emailExist(email) {
  return request({
    url: '/login/checkIfTheEmailExists',
    method: 'post',
    data: {email},
  })
}

export function getCode(email) {
  return request({
    url: '/login/sendEmailVerificationCode',
    method: 'post',
    data: {email},
  })
}

export function addUser(name, pwd, email) {
  return request({
    url: '/register/userRegistration',
    method: 'post',
    data: {name, pwd, email},
  })
}
