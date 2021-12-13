import axios from "axios";

axios.defaults.headers = {
    "Content-Type": "application/x-www-form-urlencoded;charset=UTF-8",
    "token":localStorage.getItem("token") || ""
}

axios.defaults.transformRequest = [function (data) {
    var newData = "";
    for (var k in data) {
        newData += encodeURIComponent(k) + '=' + encodeURIComponent(data[k]) + '&'
    }
    return newData
}]

export function request(config) {
    const instance = axios.create({
        baseURL:'your_url',
        timeout:5000
    })

    //请求拦截器
    instance.interceptors.request.use(config => {
        // 操作，比如config 信息不符合要求 ...
        // 显示loading图标

        return config
    }, error => {

    });

    //相应拦截器
    instance.interceptors.response.use(res => {
        return res
    }, error => {

    });

    return instance(config)
}
