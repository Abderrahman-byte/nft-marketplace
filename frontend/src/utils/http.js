export const DEFAULT_HEADERS = { 'Content-Type': 'application/json' }

export const httpRequest = async (url, method, data = null, headers = {}, options = {}) => {
    const requestOptions = {
        credentials: 'include',
        headers : { ...DEFAULT_HEADERS, ...headers},
        method,
        ...options
    }


    if (method !== 'GET' && method !== 'HEAD') requestOptions.body = data

    const response = await fetch(url, requestOptions)

    const contentType = response.headers.get('Content-Type') || response.headers.get('content-type')
    const responseData = /json/.test(contentType) ? await response.json() : await response.text()

    return responseData
}

export const multipartPostRequest = async (url, data, options) => {
    const response = await fetch(url, {
        credentials: 'include',
        method:  'POST',
        body: data,
        ...options
    })

    const contentType = response.headers.get('Content-Type') || response.headers.get('content-type')
    const responseData = /json/.test(contentType) ? await response.json() : await response.text()

    return responseData
}

export const getRequest = (url, headers = {}, options = {}) => httpRequest(url, 'GET', headers, options)

export const deleteRequest = (url, headers = {}, options = {}) => httpRequest(url, 'DELETE', headers, options)

export const postRequest = (url, data, headers = {}, options = {}) => httpRequest(url, 'POST', data, headers, options)