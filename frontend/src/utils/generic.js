import { formatMoney } from "./currency"

export const DEFAULT_ERROR = {
    field: null,
    message: 'Something went wrong, please try again another time'
}

export const ONE_SECOND = 1000
export const ONE_MINUTE = 60 * ONE_SECOND
export const ONE_HOUR = 60 * ONE_MINUTE
export const ONE_DAY = 24 * ONE_HOUR
export const ONE_WEEK = 7 * ONE_DAY
export const ONE_MONTH = 30 * ONE_DAY

export const buildPath = (...args) => {
	return args
		.map((part, i) => {
			if (i === 0) {
				return part.trim().replace(/[\/]*$/g, '')
			} else {
				return part.trim().replace(/(^[\/]*|[\/]*$)/g, '')
			}
		})
		.filter((x) => x.length)
		.join('/')
}

export const translateError = (error) => {
    if (error.title === 'invalid_data') {
        return error.invalidFields.map(err => {
            return { field: err.name, message: err.reason }
        })
    } 

    if (error.title === 'wrong_credentials') {
        return [{field: null, message: 'Username or password are wrong.' }]
    }

    if (error.title === 'data_integrety_error') {
        return [{ field: error.field, message: error.detail }]
    }

    if (error.detail) {
        return [{ field: null, message: error.detail}]
    }

    return [DEFAULT_ERROR]
}

export const formatDate = (timestamp) => {
    const date = timestamp ? new Date(timestamp) : new Date()
    const monthStr = date.toLocaleString('default', { month: 'long' });

    return `${monthStr} ${date.getDate()}, ${date.getFullYear()}`
}

export const formatBigNumberMoney = (n) => {
    if (n >= 1000000) {
        return (n / 1000000).toFixed(1) + 'M'
    }

    if (n >= 100000) {
        return (n / 1000).toFixed(1) + 'k'
    }


    return formatMoney(n)
}   

export const plural = (singular, count) => {
    const prep = ['a', 'e', 'u', 'i', 'o', 'h'].some(v => singular.startsWith(v)) ? 'an ' : 'a ' 
    return count === 1 ? prep + singular : count + ' ' + singular + 's'
}

export const formatLapse = (timestamp) => {
    const lapse = Date.now() - timestamp

    const minutes = Number.parseInt(lapse / ONE_MINUTE)
    const hours = Number.parseInt(lapse / ONE_HOUR)
    const days = Number.parseInt(lapse / ONE_DAY)
    const weeks = Number.parseInt(lapse / ONE_WEEK)
    const months = Number.parseInt(lapse / ONE_MONTH)


    if (months >= 12) formatDate(timestamp)
    if (months > 0) return plural('month', months) + ' ago'
    if (weeks > 0) return plural('week', weeks) + ' ago'
    if (days > 0) return plural('day', days) + ' ago'
    if (hours > 0) return plural('hour', hours) + ' ago'
    if (minutes > 0) return plural('minute', minutes) + ' ago'
    
    return 'a few seconds'
}