export const DEFAULT_ERROR = {
    field: null,
    message: 'Something went wrong, please try again another time'
}

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

    return [DEFAULT_ERROR]
}