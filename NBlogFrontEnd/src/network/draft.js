import {request} from "./request";

export function getAllDraft(name) {
  return request({
    url:'article/getAllDraft',
    method:'post',
    data: {name}
  })
}

export function deleteDraft(did) {
  return request({
    url:'article/deleteDraft',
    method:'post',
    data: {did}
  })
}


