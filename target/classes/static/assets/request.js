(function () {
    // 对外暴露的API调用接口
    window.api = {
        get(url) {
            return request(url, {method: "GET"});
        },
        post(url, body) {
            return request(url, {method: "POST", body});
        },
        put(url, body) {
            return request(url, {method: "PUT", body});
        },
        delete(url) {
            return request(url, {method: "DELETE"});
        }
    };

    async function request(url, options) {
        const response = await ajax(url, options); // 发送ajax请求
        const payload = parseJson(response.text); // 解析JSON响应

        // 前端调用api接口时，未登录状态兜底处理
        if (response.status === 401 && window.location.pathname !== "/login.html") {
            window.location.href = "/login.html";
            throw new Error("请先登录");
        }
        // 其他异常状态码处理
        if (response.status < 200 || response.status >= 300 || payload.success === false) {
            throw new Error(payload.message || "请求失败");
        }
        return payload.data; // 返回成功响应数据
    }

    // ajax请求封装：把 XMLHttpRequest 包装成 Promise，方便外层使用 await
    function ajax(url, options) {
        return new Promise((resolve, reject) => { // Promise 用来表示一个异步请求最终会成功或失败
            const xhr = new XMLHttpRequest(); // 创建原生 Ajax 请求对象
            xhr.open(options.method || "GET", url, true); // 设置请求方式(默认get)、请求地址；true 表示异步请求
            xhr.withCredentials = true; // 允许请求携带同源 Cookie，例如登录后的 JSESSIONID

            const headers = Object.assign({}, options.headers || {}); // 复制调用方传入的请求头；没传就使用空对象
            let body = null; // 默认没有请求体，GET 和 DELETE 通常就是 null
            if (options.body !== undefined) { // 如果调用方传了 body，说明这次请求需要发送 JSON 数据
                headers["Content-Type"] = "application/json"; // 告诉后端请求体格式是 JSON
                body = JSON.stringify(options.body); // 把 JS 对象转换成 JSON 字符串，才能发送给后端
            }

            Object.keys(headers).forEach((name) => { // 遍历所有请求头名称
                xhr.setRequestHeader(name, headers[name]); // 把每个请求头设置到 xhr 请求对象上
            });

            xhr.onload = () => { // 后端有响应时触发，不管状态码是 200 还是 401/500 都会走这里
                resolve({ // 请求完成，把状态码和响应文本交给外层 request 方法继续处理
                    status: xhr.status, // HTTP 状态码，例如 200、401、404、500
                    text: xhr.responseText // 后端返回的原始字符串，通常是一段 JSON 字符串
                });
            };
            xhr.onerror = () => reject(new Error("网络请求失败")); // 网络层面失败时触发，例如服务器连不上
            xhr.ontimeout = () => reject(new Error("请求超时")); // 请求超时时触发；当前没有设置 timeout，默认一般不会触发
            xhr.send(body); // 真正发送请求；GET/DELETE 发送 null，POST/PUT 发送 JSON 字符串
        });
    }

    // 解析JSON响应，统一异常处理
    function parseJson(text) {
        try {
            return JSON.parse(text);
        } catch (error) {
            return {
                success: false,
                message: "服务响应格式异常"
            };
        }
    }
})();
