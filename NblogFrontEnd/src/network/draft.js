import {request} from "./request";

export function getAllDraft() {
  return request({
    url: '/article/getAllDraft',
    method: 'post',
  })
}

export function deleteDraft(did) {
  return request({
    url: '/article/deleteDraft',
    method: 'post',
    data:{did}
  })
}

export function getReDraftInfo(aid) {
  return request({
    url: '/article/getReDraftInfo',
    method: 'post',
    data:{aid}
  })
}
