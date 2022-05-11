export const DEFAULT_ERROR = {
    field: null,
    message: 'Something went wrong, please try again another time'
}

export const ONE_DAY = 24 * 60 * 60 * 1000

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
    console.log(timestamp)
    const date = timestamp ? new Date(timestamp) : new Date()
    const monthStr = date.toLocaleString('default', { month: 'long' });

    return `${monthStr} ${date.getDate()}, ${date.getFullYear()}`
}