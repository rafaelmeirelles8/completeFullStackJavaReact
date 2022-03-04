import { Badge} from 'antd';

function StudentsCountBadge({count}) {
    return (
            <Badge count={count} showZero="true" style={{ backgroundColor: '#A5ADAD' }}/>
    );
};

export default StudentsCountBadge;