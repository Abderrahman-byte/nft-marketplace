import React, { createRef, useEffect } from 'react'

import './styles.css'

const Slider = ({min, max, onChange, value}) => {
    const inputRef = createRef()

    const changeCallback = e => {
        e.target.style.setProperty('--value', e.target.value)
        onChange(Number.parseInt(e.target.value) || 0)
    }

    useEffect(() => {
        if (!inputRef.current) return

        const target = inputRef.current

        target.style.setProperty('--value', target.value)
        target.style.setProperty('--min', 1)
        target.style.setProperty('--max', 100)
    }, [inputRef.current])

    return (
        <div className='Slider'>
            <input ref={inputRef} onChange={changeCallback} value={value} className='slider' type='range' min={min} max={max} />
            <div className='values'>
                <span>{min} RVN</span>
                <span>{max} RVN</span>
            </div>
        </div>
    )
}

export default Slider