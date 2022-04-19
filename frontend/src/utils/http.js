const DEFAULT_HEADERS = { 'Content-Type': 'application/json' }

export const httpRequest = async (url, method, data = null, headers = {}, options = {}) => {
    const response = await fetch(url, {
        credentials: 'include',
        body: data,
        headers : { ...DEFAULT_HEADERS, ...headers},
        method,
        ...options
    })

    const contentType = response.headers.get('Content-Type') || response.headers.get('content-type')
    const responseData = /json/.test(contentType) ? await response.json() : await response.text()

    return responseData
}

export const getRequest = (url, headers = {}, options = {}) => httpRequest(url, 'GET', headers, options)

export const postRequest = (url, data, headers = {}, options = {}) => httpRequest(url, 'POST', data, headers, options)