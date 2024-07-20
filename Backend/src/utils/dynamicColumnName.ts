export const getNthColumnName = async (n: number, repo: any) => {
    const entityMetadata = repo.metadata;

    if (n < 0 || n >= entityMetadata.columns.length) {
        return `Column index ${n} is out of bounds for entity.`;
    }

    const column = entityMetadata.columns[n];

    return column.propertyName
}