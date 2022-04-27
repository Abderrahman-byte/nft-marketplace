import React from 'react'

import '../../styles/CheckboxSettingsItem.css'

const CheckboxSettingsItem = ({title, subtitle, name, defaultValue = false}) => {

    return (
        <div className='CheckboxSettingsItem'>
            <div className='info'>
                <label className='CheckboxSettingsItem-label'>{title}</label>
                <small className='CheckboxSettingsItem-subtitle'>{subtitle}</small>
            </div>

            <div className='input'>
                <input id={'checkbox-' + name} className='checkbox' name={name} type='checkbox' defaultChecked={defaultValue} />
                <label htmlFor={'checkbox-' + name} className='slider'/>
            </div>

        </div>
    )
}

export default CheckboxSettingsItem