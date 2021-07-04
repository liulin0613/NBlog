import {request} from "./request";

export function getDetailsById(id) {
  return request({
    url:'article/getDetailsById',
    method:'post',
    data: {id}
  })
}
