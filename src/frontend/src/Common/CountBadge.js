import { Badge} from 'antd';

function CountBadge({count}) {
    return (
            <Badge count={count} showZero="true" style={{ backgroundColor: '#A5ADAD' }}/>
    );
};

export default CountBadge;