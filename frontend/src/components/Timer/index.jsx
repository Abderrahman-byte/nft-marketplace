import React, { useCallback, useEffect, useState } from 'react'

import './styles.css'

const Timer = ({ date }) => {
    const [endDate] = useState(date || new Date())
    const [timerData, setTimerData] = useState({})

    const updateTimer = useCallback(() => {
        const intervale = new Date(endDate - new Date())

        const seconds = intervale.getSeconds()
        const minutes = intervale.getMinutes()
        const hours = intervale.getHours()

        setTimerData({'Hrs': hours, 'mins': minutes, 'secs': seconds})
    }, [endDate])
    
    useEffect(() => {
        updateTimer()

        const timer = setInterval(updateTimer, [])

        return () => clearInterval(timer)
    }, [])

    return (
        <div className='Timer'>
            {Object.entries(timerData).map(entry => (
                <div className='Timer-section'>
                    <span className='value'>{entry[1]}</span>
                    <span className='key'>{entry[0]}</span>
                </div>
            ))}
        </div>
    )
}

export default Timer