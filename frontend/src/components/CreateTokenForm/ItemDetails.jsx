import React from 'react'

import '@Styles/ItemDetails.css'

const ItemDetails = () => {
    return (
        <div className='ItemDetails form-div'>
            <label className='section-label'>Item Details</label>

            <div className='form-subdiv'>
                <label className='form-label'>Item name</label>
                <input className='input-elt' type='text' name='item-name' autoComplete='off' 
                    placeholder='e. g. "Redeemable Bitcoin Card with logo"' />
            </div>

            <div className='form-subdiv'>
                <label className='form-label'>descrition</label>
                <input className='input-elt' type='text' name='description' autoComplete='off'
                    placeholder='e. g. “After purchasing you will able to recived the logo...”' />
            </div>

            <div className='form-subdiv'>
                <label className='form-label'>Royalties</label>
                <input className='input-elt' type='number' defaultValue={10} name='royalties' max={50} min={0} />
            </div>
        </div>
    )
}

export default ItemDetails