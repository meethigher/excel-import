package top.meethigher.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import top.meethigher.domain.entity.Doc;
/**
 * 档案
 *
 * @author chenchuancheng github.com/meethigher
 * @since 2022/7/30 09:09
 */
public interface DocRepository extends JpaRepository<Doc, String>, JpaSpecificationExecutor<Doc> {
}
