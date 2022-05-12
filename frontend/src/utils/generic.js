import { formatMoney } from "./currency"

export const DEFAULT_ERROR = {
    field: null,
    message: 'Something went wrong, please try again another time'
}

export const ONE_HOUR = 60 * 60 * 1000
export const ONE_DAY = 24 * ONE_HOUR

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