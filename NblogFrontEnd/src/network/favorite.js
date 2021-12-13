import {request} from "./request";

export function getAllFavorites() {
  return request({
    url: '/article/getAllFavorites',
    method: 'post',
  })
}
