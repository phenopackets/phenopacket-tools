yaml/%: %
	mkdir -p docs/$@ && cp `find $< -type f -name "*.yml"` docs/$@ 
#	 for i in $@/*.yml; do yamldoc "$$i" > "$$i".md; done

# static/%: docs/tmp/%
# 	mkdir -p docs/$@ && cp `find $< -type f -name "*.md"` docs/$@


# static/%: %
# 	mkdir -p docs/$@ && find $< -type f -name "*.yml" -exec yamldoc {} > docs/$@/{}.md \;

yaml-docs: yaml/families yaml/phenopackets