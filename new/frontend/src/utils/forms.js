export const validateForm = (data, fields) => {
	const errors = []

	fields.forEach(field => {
        const value = data[field.name]

        if (field.isRequired && (!value || value === '')) {
            errors.push({
                field: field.name,
                message: `The ${field.displayName} field is required.`
            })

            return
        }

        if (!field.rules) return

        for (let rule of field.rules) {
            if (!rule.regex.test(value)) {
                errors.push({ field: field.name, message: rule.message})
            }
        }
       
    })

	return errors
}

export const registerFormFields = [
	{
		name: 'username',
		displayName: 'Username',
        isRequired: true,
		rules: [
			{
				regex: /^[A-Za-z].{4,}$/,
				message: 'The username must start only with letters and must contain at least 5 characters.',
			},
		],
	},
	{
		name: 'email',
		displayName: 'Email address',
        isRequired: true,
		rules: [
			{
				regex: /^(?=.{1,64}@)[A-Za-z0-9_-]+(\.[A-Za-z0-9_-]+)*@[^-\.][A-Za-z0-9-]+(\.[A-Za-z0-9-]+)*(\.[A-Za-z]{2,})$/,
				message: 'Invalid email address',
			},
		],
	},
	{
		name: 'password',
		displayName: 'Password',
        isRequired: true,
		rules: [
			{
				regex: /(?=.*[A-Z].*)(?=.*[a-z].*)(?=.*[0-9].*)(?=.{8,})/,
				message: 'The password minimum eight characters, at least one uppercase letter, one lowercase letter and one number.',
			},
		],
	},
    {
        name: 'password2',
        displayName: 'Password confirmation',
        isRequired: true,
        rules: []
    }
]
