import React from 'react'

import './styles.css'

const CheckboxSettingsItem = ({title, subtitle, name, defaultValue = false, ...rest}) => {

    return (
        <div className='CheckboxSettingsItem'>
            <div className='info'>
                <label className='CheckboxSettingsItem-label'>{title}</label>
                <small className='CheckboxSettingsItem-subtitle'>{subtitle}</small>
            </div>

            <div className='input'>
                <input id={'checkbox-' + name} className='checkbox' name={name} type='checkbox' defaultChecked={defaultValue} {...rest} />
                <label htmlFor={'checkbox-' + name} className='slider'/>
            </div>

        </div>
    )
}

export default CheckboxSettingsItem